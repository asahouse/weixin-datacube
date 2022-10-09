package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.async.FutureJob;
import com.andc.amway.datacubecatcher.service.impl.*;
import com.andc.amway.datacubecatcher.service.inter.CatchService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/4.
 * 抓取数据管理器
 */
@Service
public class CatchManager {

//    @Autowired
//    CatchEventLogRepository catchEventLogRepository;

    @Autowired
    ArticleTotalServiceImpl articleTotalService;

    @Autowired
    ArticleSummaryServiceImpl articleSummaryService;

    @Autowired
    UserReadServiceImpl userReadService;

    @Autowired
    UserReadHourServiceImpl userReadHourService;

    @Autowired
    UserShareServiceImpl userShareService;

    @Autowired
    UserShareHourServiceImpl userShareHourService;

    @Autowired
    UserSummaryServiceImpl userSummaryService;

    @Autowired
    UserCumulateServiceImpl userCumulateService;

    @Autowired
    InterfaceSummaryServiceImpl interfaceSummaryService;

    @Autowired
    InterfaceSummaryHourServiceImpl interfaceSummaryHourService;

    @Autowired
    UpStreamMsgServiceImpl upStreamMsgService;

    @Autowired
    UpStreamMsgHourServiceImpl upStreamMsgHourService;

    @Autowired
    UpStreamMsgWeekServiceImpl upStreamMsgWeekService;

    @Autowired
    UpStreamMsgMonthServiceImpl upStreamMsgMonthService;

    @Autowired
    UpStreamMsgDistServiceImpl upStreamMsgDistService;

    @Autowired
    UpStreamMsgDistWeekServiceImpl upStreamMsgDistWeekService;

    @Autowired
    UpStreamMsgDistMonthServiceImpl upStreamMsgDistMonthService;

    @SneakyThrows
    public CatchService catchServiceInstance(WxApiType type){
        if (type.equals(WxApiType.GetArticleTotal))
            return articleTotalService;
        if (type.equals(WxApiType.GetArticleSummary))
            return articleSummaryService;
        if (type.equals(WxApiType.GetUserRead))
            return userReadService;
        if (type.equals(WxApiType.GetUserReadHour))
            return userReadHourService;
        if (type.equals(WxApiType.GetUserShare))
            return userShareService;
        if (type.equals(WxApiType.GetUserShareHour))
            return userShareHourService;
        if (type.equals(WxApiType.GetUserSummary))
            return userSummaryService;
        if (type.equals(WxApiType.GetUserCumulate))
            return userCumulateService;
        if (type.equals(WxApiType.GetUpStreamMsg))
            return upStreamMsgService;
        if (type.equals(WxApiType.GetUpStreamMsgHour))
            return upStreamMsgHourService;
        if (type.equals(WxApiType.GetUpStreamMsgWeek))
            return upStreamMsgWeekService;
        if (type.equals(WxApiType.GetUpStreamMsgMonth))
            return upStreamMsgMonthService;
        if (type.equals(WxApiType.GetUpStreamMsgDist))
            return upStreamMsgDistService;
        if (type.equals(WxApiType.GetUpStreamMsgDistWeek))
            return upStreamMsgDistWeekService;
        if (type.equals(WxApiType.GetUpStreamMsgDistMonth))
            return upStreamMsgDistMonthService;
        if (type.equals(WxApiType.GetInterfaceSummary))
            return interfaceSummaryService;
        if (type.equals(WxApiType.GetInterfaceSummaryHour))
            return interfaceSummaryHourService;

        throw new IllegalAccessException("Type does not exist WxApiType impl!");
    }




    @Transactional
    @SneakyThrows
    public List<FutureJob> execute(WxApiType type, LocalDate start, LocalDate end){
        return this.execute(Collections.emptyList(), type, start, end);
    }

    @Transactional
    @SneakyThrows
    public List<FutureJob> execute(List<String> ids, List<WxApiType> types, LocalDate start, LocalDate end){
        if (types.isEmpty()) return new ArrayList<>();
        return types.parallelStream()
                .flatMap(type -> this.execute(ids, type, start, end).stream())
                .collect(Collectors.toList());
    }

    @Transactional
    @SneakyThrows
    public List<FutureJob> execute(List<String> ids, WxApiType type, LocalDate start, LocalDate end){
        List<FutureJob> workers = this.catchServiceInstance(type).catching(ids, start, end);
        return workers;
    }

    @Transactional
    @SneakyThrows
    public List<FutureJob> execute(String id, WxApiType type, LocalDate start, LocalDate end){
        List<FutureJob> workers = this.catchServiceInstance(type).catching(Collections.singletonList(id), start, end);
        return workers;
    }



    //保障发起请求的记录正确性, 若返回后不进行更新插入,可使用以下定时器更新 (300000 = 5min)
    /*
    @Scheduled(fixedRate = 300000)
    public void scheduleToCheckProcessing(){
        List<CatchEventLog> catchEventLogs = catchEventLogRepository.findByCreateTimeAfter(
                LocalDateTime.now().withHour(0).withMinute(0).withSecond(0));

        catchEventLogs.stream()
                .filter(log ->
                        log.getEventStatus().equals(CatchEventLog.EventStatus.init)
                        || log.getEventStatus().equals(CatchEventLog.EventStatus.processing))
                .forEach(log ->{

            if (futures.containsKey(log.getThreadName())){
                Future future = futures.get(log.getThreadName());
                //同时监察状态变更
                if (future.isDone() && !future.isCancelled())
                    log.setEventStatus(CatchEventLog.EventStatus.complete);
                else if (!future.isDone() && future.isCancelled())
                    log.setEventStatus(CatchEventLog.EventStatus.abort);

                futures.remove(log.getThreadName());//缓存中去除已记录
            }else
                log.setEventStatus(CatchEventLog.EventStatus.remove);

            catchEventLogRepository.save(log);
        });

        //去除错误的缓存线程
        catchEventLogs.stream()
                .filter(log -> log.getEventStatus().equals(CatchEventLog.EventStatus.error))
                .forEach(log -> {
                    futures.remove(log.getThreadName());//缓存中去除已记录
                });
    }*/
}
