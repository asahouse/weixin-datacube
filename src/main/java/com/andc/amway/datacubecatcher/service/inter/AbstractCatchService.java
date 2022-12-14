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




    //??????????????????
    protected WxApiType currentType;

    //????????????????????????????????????
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

                    //??????[?????????????????????], ?????????????????????, ???????????????????????????, impl???????????????
                    if (!currentType.equals(WxApiType.GetArticleTotal))
                        filth = planManager.achievePlanDatasStatusByWxApi(account, currentType, durtionDates)
                                .entrySet().stream().filter(col -> col.getValue().equals(PlanManager.PlanStatus.CATCH))
                                .map(col -> col.getKey()).collect(Collectors.toSet());

                    //???List<DatePair>?????????,??????????????????????????????????????????????????????
                    List<DatePair> multiPairs = KCDateTimeUtils.mercy(KCDateTimeUtils.healRugged(
                            KCDateTimeUtils.wipeout(durtionDates, filth)));

                    log.info("currentType -> "+currentType);
                    log.info("pair -> "+pair.toString());
                    log.info("multiPairs -> "+JSON.toJSONString(multiPairs));

                    //?????????Pair????????????,???????????????????????????,????????????Pair
                    return multiPairs.parallelStream().map(singlePair ->{
                        final String threadName = CatchEventLog.generateThreadName();

                        /**
                         * ??????CompleteFuture????????????
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
        //?????????????????? ??? ??????????????????
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

            //???????????? -> ????????????
            insideLog.setEventStatus(CatchEventLog.EventStatus.processing);

            //??????????????????????????????
            if (countValue>=limitValue)
                throw new IllegalStateException("Api Request Over limit!!");

            /**
             * ???????????? IO
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
             * ???????????????????????????????????? IO
             */
            //????????????????????????
            Set<String> duringDates = KCDateTimeUtils.achieveCalendar(
                    pair.getStart().toLocalDate(), pair.getEnd().toLocalDate())
                    .parallelStream().map(d->d.toString()).collect(Collectors.toSet());


            /**
             * ???????????? -> ???????????? IO
             */
            JSONObject collection = (JSONObject) JSONObject.toJSON(result);
            JSONArray list = collection.getJSONArray("list");

            /**
             * process          -> ??????JSON??????
             * filterDuplicate  -> ??????????????????/??????
             * save             -> ?????????????????????
             **/
            List<? extends AbstractEntity> afterProcessing = this.process(account.getId(), list);

            //??????????????????????????? IO
            planManager.recordPlanDataStatusByWxApi(account, currentType, duringDates, PlanManager.PlanStatus.VISITED);
            //??????????????????????????????????????? IO
            planManager.recordPlanDataStatusByWxApi(account, currentType,
                    afterProcessing.stream().collect(groupingBy(AbstractEntity::getRef_date))
                            .keySet(), PlanManager.PlanStatus.CATCH);

            /**
             * ????????????[??????????????????],impl?????????????????????????????????ref????????????
             */
            List<? extends AbstractEntity> entities;
            if (this instanceof ArticleTotalServiceImpl)
                entities = this.save(account.getId(), afterProcessing);
            else
                entities = this.save(account.getId(), this.filterDuplicate(afterProcessing, account.getId()));


            //??????????????????
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

            //????????????
            insideLog.setEventDetailError(ex.getMessage());
            insideLog.setEventStatus(CatchEventLog.EventStatus.error);
        } finally {
            /**
             * ???????????? IO
             */
            catchEventLogRepository.save(insideLog);

            //??????????????????
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

        //???ref_date??????
        Map<String, List<AbstractEntity>> grouping =
                originals.parallelStream().collect(
                        Collectors.groupingBy(AbstractEntity::getRef_date));

        if (grouping.isEmpty()) return (List<R>)result;

        //DB ref_date???????????????
        Map<String, List<AbstractEntity>> exists =
                currentDataRepository.findByAccountIdAndRefDate(accountId, grouping.keySet())
                        .parallelStream().collect(Collectors.groupingBy(AbstractEntity::getRef_date));

        //obviate ??? RefDate / RefHour?????????????????????????????????, ?????????DB??????????????????????????????
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
