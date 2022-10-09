package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.service.ReportManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    ReportManager reportManager;

    @ApiOperation(value = "获取指定公众号分派ID下各个接口抓取数据", notes = "含分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "api", value = "微信接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "page", value = "分页排序,page/size/sort", required = false, dataType = "Pageable")
    })
    @GetMapping
    public Response report(@RequestParam String api,
                           @RequestParam String accountId,
                           @RequestParam String start,
                           @RequestParam String end,
                           Pageable page) {
        return Response.ok("body", reportManager.report(api, accountId, start, end, page));
    }


    @ApiOperation(value = "趋势数据->粉丝量", notes = "公众号总粉丝数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("trendForFans")
    public Response trendForFans(@RequestParam String accountId,
                                 @RequestParam String start,
                                 @RequestParam String end) {
        return Response.ok("body", reportManager.trendAtFans(accountId, start, end));
    }

    @ApiOperation(value = "趋势数据->阅读量", notes = "图文原始页的阅读数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("trendForRead")
    public Response trendForRead(@RequestParam String accountId,
                                 @RequestParam String start,
                                 @RequestParam String end) {
        return Response.ok("body", reportManager.trendAtRead(accountId, start, end));
    }

    @ApiOperation(value = "趋势数据->收藏量", notes = "添加收藏数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("trendForAdd")
    public Response trendForAdd(@RequestParam String accountId,
                                @RequestParam String start,
                                @RequestParam String end) {
        return Response.ok("body", reportManager.trendAtAdd(accountId, start, end));
    }

    @ApiOperation(value = "趋势数据->分享量", notes = "分享数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("trendForShare")
    public Response trendForShare(@RequestParam String accountId,
                                  @RequestParam String start,
                                  @RequestParam String end) {
        return Response.ok("body", reportManager.trendAtShare(accountId, start, end));
    }


    @ApiOperation(value = "粉丝数据列表", notes = "包含渠道信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("fansData")
    public Response reportForFansData(@RequestParam String accountId,
                                      @RequestParam String start,
                                      @RequestParam String end,
                                      Pageable pageable) {
        return Response.ok("body", reportManager.reportAtFansData(accountId, start, end, pageable));
    }

    @ApiOperation(value = "粉丝互动列表", notes = "包含详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("fansInteraction")
    public Response reportForFansInteraction(@RequestParam String accountId,
                                      @RequestParam String start,
                                      @RequestParam String end,
                                      Pageable pageable) {
        return Response.ok("body", reportManager.reportAtFansInteraction(accountId, start, end, pageable));
    }

    @ApiOperation(value = "图文推送列表", notes = "连同输出比率数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenPush")
    public Response reportForTuwenPush(@RequestParam String accountId,
                                       @RequestParam String start,
                                       @RequestParam String end,
                                       Pageable pageable) {
        return Response.ok("body", reportManager.reportAtTuwenPush(accountId, start, end, pageable));
    }

    @ApiOperation(value = "图文阅读/分享/收藏列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenReadAndShareAndAdd")
    public Response reportForTuwenReadAndShareAndAdd(@RequestParam String accountId,
                                                     @RequestParam String start,
                                                     @RequestParam String end,
                                                     Pageable pageable) {
        return Response.ok("body", reportManager.reportAtTuwenReadAndShareAndAdd(accountId, start, end, pageable));
    }

    @ApiOperation(value = "图文阅读/分享/收藏总数据", notes = "单日总量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("tuwenReadAndShareAndAdd/statistics")
    public Response statisticsForTuwenReadAndShareAndAdd(@RequestParam String accountId,
                                                         @RequestParam String start,
                                                         @RequestParam String end){
        return Response.ok("body", reportManager.statisticsForTuwenReadAndShareAndAdd(accountId, start, end));
    }

    @ApiOperation(value = "系统抓取日志", notes = "每次网络请求均形成日志输出, 包括DIGI / WX")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping("catchEventLog")
    public Response reportForCatchEventLog(@RequestParam String accountId,
                                           @RequestParam String start,
                                           @RequestParam String end,
                                           Pageable pageable){
        return Response.ok("body", reportManager.reportAtCatchEventLog(accountId, start, end, pageable));
    }
}
