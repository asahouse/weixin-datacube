package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.async.FutureJob;
import com.andc.amway.datacubecatcher.persistent.dao.CatchEventLogRepository;
import com.andc.amway.datacubecatcher.service.CatchManager;
import com.andc.amway.datacubecatcher.service.ScheduleManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@RestController
@RequestMapping("/api/catch")
public class CatchController {

    @Autowired
    CatchEventLogRepository catchEventLogRepository;

    @Autowired
    CatchManager catchManager;

    @Autowired
    ScheduleManager scheduleManager;

    @GetMapping("time")
    public Response time(){
        List<Object[]> result = catchEventLogRepository.time();
        Object[] dbtime = new Object[]{};
        if (!result.isEmpty()) dbtime = result.get(0);

        LocalDateTime time = LocalDateTime.now();
        return Response.ok("time", time).add("string", time.toString()).add("db", dbtime);
    }

    @GetMapping("yesterday")
    public Response scheduleToCatchYesterdayData(){
        scheduleManager.scheduleToCatchYesterdayData();
        return Response.ok();
    }

    @GetMapping("updateArticle")
    public Response scheduleToUpdateArticle(){
        scheduleManager.scheduleToUpdateArticle();
        return Response.ok();
    }

    @ApiOperation(value = "发起多个公众号分派ID下,指定的接口抓取指定几个月的数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "api", value = "接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accountIds", value = "公众号分派ID集合", required = true, dataType = "String"),
            @ApiImplicitParam(name = "range", value = "年-月为单位,逗号分隔(2017-09,2016-11)", required = true, dataType = "String[]"),
            @ApiImplicitParam(name = "apis", value = "接口集合", required = true, dataType = "String[]")
    })
    @GetMapping("condition")
    public Response catchdateByMonthAtApis(@RequestParam Set<String> accountIds,
                                           @RequestParam Set<String> range,
                                           @RequestParam Set<String> apis){
        //循环Api集合, 每个指定年月执行, 每月的首日到最后一日
        List<FutureJob> execute =
                accountIds.parallelStream().flatMap(accountId ->
                apis.parallelStream().flatMap(api ->
                range.parallelStream().flatMap(yearMonth -> {

            String[] ym = yearMonth.split("-");
            if (ym.length==2) {
                LocalDate firstDay = LocalDate.of(Integer.valueOf(ym[0]), Integer.valueOf(ym[1]), 1);

                LocalDate lastDay = Integer.valueOf(ym[1]) == LocalDate.now().getMonthValue() ?
                        firstDay.withDayOfMonth(LocalDate.now().getDayOfMonth() - 1) :
                        firstDay.withDayOfMonth(firstDay.lengthOfMonth()) ;

                return catchManager.execute(accountId, WxApiType.fromValue(api), firstDay, lastDay).stream();
            }else{
                return new ArrayList<FutureJob>().stream();
            }
        }))).collect(Collectors.toList());

//        List<FutureJob> execute = apis.parallelStream().flatMap(api -> range.parallelStream().flatMap(yearMonth -> {
//            String[] ym = yearMonth.split("-");
//            if (ym.length==2) {
//                LocalDate firstDay = LocalDate.of(Integer.valueOf(ym[0]), Integer.valueOf(ym[1]), 1);
//
//                LocalDate lastDay = Integer.valueOf(ym[1]) == LocalDate.now().getMonthValue() ?
//                        firstDay.withDayOfMonth(LocalDate.now().getDayOfMonth() - 1) :
//                        firstDay.withDayOfMonth(firstDay.lengthOfMonth()) ;
//
//                return catchManager.execute(accountId, WxApiType.fromValue(api), firstDay, lastDay).stream();
//            }else{
//                return new ArrayList<FutureJob>().stream();
//            }
//        })).collect(Collectors.toList());

        return Response.ok("result", execute);
    }

    @ApiOperation(value = "发起指定公众号分派ID下各个接口抓取数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "api", value = "接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "begin", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping
    public Response catchdata(@RequestParam String api,
                            @RequestParam String accountId,
                            @RequestParam String begin,
                            @RequestParam String end){
        List<FutureJob> execute = catchManager.execute(accountId, WxApiType.fromValue(api),
                LocalDate.parse(begin), LocalDate.parse(end));
        return Response.ok().add("result", execute);
    }

}
