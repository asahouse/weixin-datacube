package com.andc.amway.datacubecatcher.service.inter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.wx.api.ProxyWxApi;
import com.andc.amway.datacubecatcher.wx.api.WxApiInvokeRange;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.async.DatePair;
import com.andc.amway.datacubecatcher.async.FutureJob;
import com.andc.amway.datacubecatcher.async.FutureResult;
import com.andc.amway.datacubecatcher.persistent.dao.BasicDataRepository;
import com.andc.amway.datacubecatcher.persistent.dao.CatchEventLogRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractHourEntity;
import com.andc.amway.datacubecatcher.persistent.entity.CatchEventLog;
import com.andc.amway.datacubecatcher.service.ApiManager;
import com.andc.amway.datacubecatcher.service.PlanManager;
import com.andc.amway.datacubecatcher.service.impl.ArticleTotalServiceImpl;
import com.andc.amway.datacubecatcher.utils.KCDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Slf4j
public abstract class AbstractCatchService implements CatchService {

    @Autowired
    protected SourceManager sourceManager;

    @Autowired
    protected PlanManager planManager;

    @Autowired
    protected ApiManager apiManager;

    @Autowired
    protected CatchEventLogRepository catchEventLogRepository;

    @Autowired
    protected ProxyWxApi proxyWxApi;




    //当前微信接口
    protected WxApiType currentType;

    //当前索引日期基础仓库接口
    protected BasicDataRepository<? extends AbstractEntity> currentDataRepository;



    @Transactional
    public List<FutureJob> catching(
            List<String> ids,
            LocalDate start,
            LocalDate end){
        List<Account> accounts = Optional.ofNullable(ids).isPresent() && !ids.isEmpty() ?
                sourceManager.findAccountByIds(ids) :
                sourceManager.findAllAccounts();

        List<DatePair> pairs = KCDateTimeUtils.achieveCalendarPairs(WxApiInvokeRange.fromWxApiType(currentType).toValue(), start, end);

        List<FutureJob> futures = pairs.parallelStream()
                .flatMap(pair -> accounts.stream().flatMap(account -> {

                    Set<LocalDate> durtionDates = KCDateTimeUtils.achieveCalendar(
                            pair.getStart().toLocalDate(),
                            pair.getEnd().toLocalDate());

                    Set<LocalDate> filth = new HashSet<>();

                    //若是[图文群发总数据], 无需过滤已抓取, 可重新发起更新操作, impl中实现过滤
                    if (!currentType.equals(WxApiType.GetArticleTotal))
                        filth = planManager.achievePlanDatasStatusByWxApi(account, currentType, durtionDates)
                                .entrySet().stream().filter(col -> col.getValue().equals(PlanManager.PlanStatus.CATCH))
                                .map(col -> col.getKey()).collect(Collectors.toSet());

                    //把List<DatePair>集合中,迭代对比已抓取的日期并排除已抓取日期
                    List<DatePair> multiPairs = KCDateTimeUtils.mercy(KCDateTimeUtils.healRugged(
                            KCDateTimeUtils.wipeout(durtionDates, filth)));

                    log.info("currentType -> "+currentType);
                    log.info("pair -> "+pair.toString());
                    log.info("multiPairs -> "+JSON.toJSONString(multiPairs));

                    //从原始Pair的时段中,排除已执行的日期后,形成多个Pair
                    return multiPairs.parallelStream().map(singlePair ->{
                        final String threadName = CatchEventLog.generateThreadName();

                        /**
                         * 启用CompleteFuture执行任务
                         */
                        CompletableFuture<FutureResult> future = CompletableFuture.supplyAsync(
                                () -> this.run(singlePair, account, threadName));

                        return new FutureJob(threadName, future);
                    });
                })).collect(Collectors.toList());
        return futures;
    }

    private FutureResult run(
            DatePair pair,
            Account account,
            String threadName) {
        //获取限制次数 和 当前已调次数
        Long limitValue = apiManager.achieveWxApiLimit(
                account.getId(), currentType, LocalDate.now());
        Long countValue = apiManager.achieveWxApiCount(
                account.getId(), currentType, LocalDate.now());

        CatchEventLog insideLog = new CatchEventLog();
            insideLog.setCreateTime(LocalDateTime.now());
            insideLog.setThreadName(threadName);
            insideLog.setEventName(currentType.toValue());
            insideLog.setEventAccount(account.getId());
            insideLog.setEventStatus(CatchEventLog.EventStatus.init);
            insideLog.setEventProperties(JSON.toJSONString(pair));
            insideLog.setEventThenCount(countValue);
            insideLog.setEventThenLimit(limitValue);

        try{

            //状态记录 -> 正在处理
            insideLog.setEventStatus(CatchEventLog.EventStatus.processing);

            //接口是否已超调用限制
            if (countValue>=limitValue)
                throw new IllegalStateException("Api Request Over limit!!");

            /**
             * 请求接口 IO
             */
            Map<String, Object> result =
                    proxyWxApi.call(account, currentType,
                            pair.getStart().toLocalDate(),
                            pair.getEnd().toLocalDate());

            if (!Optional.ofNullable(result).isPresent()
                    || result.containsKey("errcode")){
                throw new IllegalAccessException(JSONObject.toJSONString(result));
            }


            /**
             * 标识登记已访问数据的接口 IO
             */
            //获取历经所有日期
            Set<String> duringDates = KCDateTimeUtils.achieveCalendar(
                    pair.getStart().toLocalDate(), pair.getEnd().toLocalDate())
                    .parallelStream().map(d->d.toString()).collect(Collectors.toSet());


            /**
             * 保存数据 -> 模板方法 IO
             */
            JSONObject collection = (JSONObject) JSONObject.toJSON(result);
            JSONArray list = collection.getJSONArray("list");

            /**
             * process          -> 处理JSON内容
             * filterDuplicate  -> 去除重复日期/时间
             * save             -> 实现类执行保存
             **/
            List<? extends AbstractEntity> afterProcessing = this.process(account.getId(), list);

            //标识登记抓取的接口 IO
            planManager.recordPlanDataStatusByWxApi(account, currentType, duringDates, PlanManager.PlanStatus.VISITED);
            //标识登记抓取并有数据的接口 IO
            planManager.recordPlanDataStatusByWxApi(account, currentType,
                    afterProcessing.stream().collect(groupingBy(AbstractEntity::getRef_date))
                            .keySet(), PlanManager.PlanStatus.CATCH);

            /**
             * 特殊处理[图文相关接口],impl中特有去重处理无法通过ref属性去重
             */
            List<? extends AbstractEntity> entities;
            if (this instanceof ArticleTotalServiceImpl)
                entities = this.save(account.getId(), afterProcessing);
            else
                entities = this.save(account.getId(), this.filterDuplicate(afterProcessing, account.getId()));


            //完成单次任务
            insideLog.setEventThenCount(insideLog.getEventThenCount()+1);
            insideLog.setEventStatus(CatchEventLog.EventStatus.complete);
            log.info("API -> "+currentType +" isEmpty : "+entities.isEmpty()+", Data : "+pair+", Process FINISHED!");
        } catch (Exception ex) {

            log.error("=== Catch Info ===");
            log.error("API : " + currentType);
            log.error("API Call limit : " + limitValue);
            log.error("API Call Count : " + countValue);
            log.error("Data : " + pair);
            log.error("Account : " + account);
            log.error("ThreadName : " + threadName);
            log.error("Ex : " + ex.toString());
            log.error("=== Catch Info end===");

            //任务错误
            insideLog.setEventDetailError(ex.getMessage());
            insideLog.setEventStatus(CatchEventLog.EventStatus.error);
        } finally {
            /**
             * 记录日志 IO
             */
            catchEventLogRepository.save(insideLog);

            //返回详细信息
            FutureResult futureResult = new FutureResult();
                futureResult.setAccount(account);
                futureResult.setWxApiType(currentType);
                futureResult.setDatePair(pair);
                futureResult.setCatchEventLog(insideLog);
            return futureResult;
        }
    }

    protected abstract List<? extends AbstractEntity> process(
            String accountId,
            JSONArray catchContent);

    protected abstract List<? extends AbstractEntity> save(
            String accountId,
            List<? extends AbstractEntity> filterContent);

    protected List<? extends AbstractEntity> obviate(
            List<? extends AbstractEntity> join,
            List<? extends AbstractEntity> exist){

        List result = new ArrayList<>();

        join.forEach(j -> {
            if (j instanceof AbstractHourEntity){
                AbstractHourEntity hourJ = (AbstractHourEntity) j;

                Optional<AbstractHourEntity> is =
                        (Optional<AbstractHourEntity>) exist.parallelStream().filter(e -> {
                            AbstractHourEntity hourE = (AbstractHourEntity) e;

                            return hourJ.getRef_date().equals(hourE.getRef_date())
                                    && hourJ.getRef_hour().equals(hourE.getRef_hour());
                        }).findFirst();
                if (!is.isPresent()) result.add(j);
            }else{
                Optional<AbstractEntity> is =
                        (Optional<AbstractEntity>) exist.parallelStream()
                                .filter(e -> j.getRef_date().equals(e.getRef_date()))
                                .findFirst();
                if (!is.isPresent()) result.add(j);
            }
        });

        join.clear();
        join.addAll(result);

        return result;
    }

    protected <R extends AbstractEntity> List<R> filterDuplicate(
            List<? extends R> originals, String accountId){
        List<AbstractEntity> result = new ArrayList<>();

        //按ref_date分组
        Map<String, List<AbstractEntity>> grouping =
                originals.parallelStream().collect(
                        Collectors.groupingBy(AbstractEntity::getRef_date));

        if (grouping.isEmpty()) return (List<R>)result;

        //DB ref_date的已有数据
        Map<String, List<AbstractEntity>> exists =
                currentDataRepository.findByAccountIdAndRefDate(accountId, grouping.keySet())
                        .parallelStream().collect(Collectors.groupingBy(AbstractEntity::getRef_date));

        //obviate 对 RefDate / RefHour不同的记录分别处理对比, 排除与DB重复后输出未录入数据
        if (exists.isEmpty()) {
            result = grouping.entrySet().parallelStream()
                    .flatMap(e -> e.getValue().stream()).collect(Collectors.toList());
        }else {
            result = grouping.entrySet()
                    .parallelStream().filter(e -> exists.containsKey(e.getKey()))
                    .parallel().peek(e -> this.obviate(e.getValue(), exists.get(e.getKey())))
                    .flatMap(e -> e.getValue().stream()).collect(Collectors.toList());
        }

        return (List<R>)result;
    }
}
