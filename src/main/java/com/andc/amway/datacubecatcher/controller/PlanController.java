package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.service.PlanManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by benjaminkc on 17/8/6.
 */
@RestController
@RequestMapping("/api/plan")
public class PlanController {

    @Autowired
    PlanManager planManager;

    @ApiOperation(value = "获取指定公众号分派ID下单个接口的日期抓取数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "api", value = "接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "start", value = "开始日期(横杠分隔)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "end", value = "结束日期(横杠分隔)", required = true, dataType = "String")
    })
    @GetMapping
    public Response find(@RequestParam String accountId,
                         @RequestParam String api,
                         @RequestParam String start,
                         @RequestParam String end){

        List<Map.Entry<String, String>> result =
                planManager.find(accountId, WxApiType.fromValue(api),
                        LocalDate.parse(start), LocalDate.parse(end));

        return Response.ok("body", result);
    }
}
