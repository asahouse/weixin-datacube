package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.wx.api.WxApiInvokeRange;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.async.DatePair;
import com.andc.amway.datacubecatcher.async.FutureJob;
import com.andc.amway.datacubecatcher.persistent.dto.ArticleTotalDetailDiffDTO;
import com.andc.amway.datacubecatcher.persistent.entity.CatchEventLog;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/8/8.
 * 关于定时任务管理器
 */
@Slf4j
@Service
public class ScheduleManager {

    @Autowired
    PlanManager planManager;

    @Autowired
    ApiManager apiManager;

    @Autowired
    CatchManager catchManager;

    @Autowired
    SourceManager sourceManager;

    @Autowired
    ReportManager reportManager;

    @Value("${weixin.datacube.startdate}")
    String weixin_datacube_startdate;

    @Value("${weixin.request-diary-rate}")
    Double weixin_request_diary_rate;

    /**
     * 计划每天更新计划日历
     * rebuild使用非覆盖新增,已有数据不会更改,只对新日期新增
     */
    @Scheduled(cron = "0 1 9 * * ?")
    public void scheduleToBuild(){
        log.info("== scheduleToBuild ==");
        /**
         * 重建数据计划日历, 微信起始日到程序启动日的日历
         * 微信数据统计接口从2014年12月1日后开始有正确的数据返回
         */
        planManager.rebuildPlanCalendar();
        planManager.rebuildPlanWxApiCalendar();//建立接口的基本状态

        /**
         * 重建所有公众号下所有接口的请求限制(保留已有,未创建的新建)
         */
        apiManager.rebuildWxApiLimit(sourceManager.findAllAccounts(),false);//建立接口的限制次数
        log.info("== scheduleToBuild end ==");
    }

    /**
     * 计划每天抓取昨天的所有公众号所有接口数据
     */
    @SneakyThrows
    @Scheduled(cron = "0 5 9 * * ?")
    public void scheduleToCatchYesterdayData(){
        log.info("== scheduleToCatchYesterdayData ==");
        LocalDate yesterday = LocalDate.now().minusDays(1);//昨天

        List<String> accounts = sourceManager.findAllAccounts()
                .parallelStream().map(account -> account.getId()).collect(Collectors.toList());
//        List<WxApiType> types = EnumSet.allOf(WxApiType.class)
//                .parallelStream().collect(Collectors.toList());

        List<WxApiType> types = new ArrayList<>();
            types.add(WxApiType.GetArticleSummary);
            types.add(WxApiType.GetArticleTotal);
            types.add(WxApiType.GetUserRead);
            types.add(WxApiType.GetUserReadHour);
            types.add(WxApiType.GetUserShare);
            types.add(WxApiType.GetUserShareHour);
            types.add(WxApiType.GetUserSummary);
            types.add(WxApiType.GetUserCumulate);
            types.add(WxApiType.GetInterfaceSummary);
            types.add(WxApiType.GetInterfaceSummaryHour);

        List<FutureJob> futureJobs = catchManager.execute(accounts, types, yesterday, yesterday);

        //异步执行所有future,但等待所有结果返回
        CompletableFuture.allOf(
                futureJobs.parallelStream().map(futureJob -> futureJob.getFuture())
                        .toArray(CompletableFuture[]::new)).join();
        log.info("== scheduleToCatchYesterdayData end ==");
    }

    /**
     * 计划每天对全局文章进行检测,数据未满7日的跟踪更新
     */
    @Scheduled(cron = "0 15 9 * * ?")
    public void scheduleToUpdateArticle(){
        log.info("== scheduleToUpdateArticle ==");

        List<String> accounts = sourceManager.findAllAccounts()
                .parallelStream().map(account -> account.getId()).collect(Collectors.toList());

        Integer diffDay = 6;
        //筛选全局中,文章数据日期与发布日期差距少于6日的进行更新
        List<ArticleTotalDetailDiffDTO> diffDTOS = reportManager.diff(diffDay);
        diffDTOS.parallelStream()
                .filter(dto->accounts.contains(dto.getAccount_id()))//过滤当前接入平台的账号
                .forEach(dto -> {

            log.info("track -> day : "+diffDay+", accountId : "+dto.getAccount_id()+", refDate : "+dto.getRef_date());
            catchManager.execute(dto.getAccount_id().toString(),
                    WxApiType.GetArticleTotal,
                    LocalDate.parse(dto.getRef_date()),
                    LocalDate.parse(dto.getRef_date()));
        });

        log.info("== scheduleToUpdateArticle end ==");
    }

    /**
     * 用于计划任务对以往数据进行计划抓取
     * 设想这样主动抓取并不理想, 较理想是开放接口, 给用户手动按需抓取
     *
     * 用上三个Redis集合进行记录
     * 1) 公众号下接口的终结日期是否到达
     * 2) 公众号下接口的历史执行日历
     * 3) 公众号下接口的执行错误集合
     */
    @SneakyThrows
    @Scheduled(cron = "0 30 9 * * ?")
    public void scheduleToCatchBeforeData(){
        log.info("== scheduleToCatchBeforeData ==");

        List<String> ids = sourceManager.findAllAccounts()
                .parallelStream().map(account -> account.getId()).collect(Collectors.toList());
//        List<WxApiType> types = EnumSet.allOf(WxApiType.class)
//                .parallelStream().collect(Collectors.toList());

        List<WxApiType> types = new ArrayList<>();
            types.add(WxApiType.GetArticleSummary);
            types.add(WxApiType.GetArticleTotal);
            types.add(WxApiType.GetUserRead);
            types.add(WxApiType.GetUserReadHour);
            types.add(WxApiType.GetUserShare);
            types.add(WxApiType.GetUserShareHour);
            types.add(WxApiType.GetUserSummary);
            types.add(WxApiType.GetUserCumulate);
            types.add(WxApiType.GetInterfaceSummary);
            types.add(WxApiType.GetInterfaceSummaryHour);

        ids.parallelStream().forEach(id ->
            types.parallelStream().forEach(type -> {

                //执行日
                LocalDate currentDay = LocalDate.now().minusDays(1);//微信不容许查询即日数据
                //微信终极日期
                LocalDate terminalDate = LocalDate.parse(weixin_datacube_startdate);
                //获取历史日历的边界日期
                Optional<LocalDate> targetDate = apiManager.achieveDiaryHistoryDate(id, type, false);
                if(targetDate.isPresent() && targetDate.get().compareTo(terminalDate)<=0) return;//终极日期到达不执行

                /**
                 * 总次数只是我们假定, 不排除其他第三方平台已使用部分可调用次数
                 * 获取当天的各个公众号的各个接口的10%调用次数(配置文件设置)   (其实并不表示真实可用的10%,没有微信接口真实的接口调用数据,只能靠近凌晨后执行较准确.)
                 * 乘 接口的单次调用范围(日) 得出 每个公众号每个接口的当前计划调用日数 -> N天
                 * 推导得出 (执行当天倒退N天->开始日期) 和 (当天为结束日期)
                 */
                Long currentApiLimit = apiManager.achieveWxApiLimit(id, type, currentDay);
                Long predictTimes = Math.round(currentApiLimit * weixin_request_diary_rate);
                WxApiInvokeRange invokeRange = WxApiInvokeRange.fromWxApiType(type);
                Long durationDays = predictTimes * invokeRange.toValue();


                LocalDate start, end;

                end = !targetDate.isPresent() ?
                        currentDay :
                        targetDate.get().minusDays(1);//历史日期最前的再推多一天

                if (end.isBefore(terminalDate)) {
                    end = terminalDate;
                    start = terminalDate;
                }else {
                    start = end.minusDays(durationDays);//倒退N天
                    if (start.isBefore(terminalDate)) start = terminalDate;
                }

                DatePair pairForError = new DatePair(start, end);//集结时间对,作记录用
                //SortedSet 记录 已执行的日期, key = Basekey+id+type , member = 日期, score = 世纪日数,下次可以排序获取最远/最近执行的日期,再开始执行
                apiManager.recordDiaryHistoryDates(id, type, start, end);

                catchManager.execute(id, type, start, end).parallelStream().map(
                        futureJob -> futureJob.getFuture().handleAsync((result, ex) -> {

                            //ex是方法抛出的错误, 若方法内已抓取错误形成输出则从返回值内获取
                            if (Optional.ofNullable(result.getCatchEventLog()).isPresent()
                                    && result.getCatchEventLog().getEventStatus().equals(
                                            CatchEventLog.EventStatus.error)){

                                String info = result.getCatchEventLog().getEventDetailError();
                                //Hash 记录 错误日期, key = id+type , field = 日期Pair , value = info, 错误日期可另作计划任务处理,或人工分析错误(接口输出错误)
                                apiManager.recordDiaryError(id, type, pairForError, info);
                            }
                            return result;
                        }));
            })
        );

        log.info("== scheduleToCatchBeforeData end ==");
    }


    //多个线程汇聚成一个线程返回所有结果
    private <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()));
    }

}
