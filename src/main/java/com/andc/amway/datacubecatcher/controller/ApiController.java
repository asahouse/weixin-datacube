package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.wx.source.digi.ProxyDigiApi;
import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiApiType;
import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.wx.api.ProxyWxApi;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.service.ApiManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Map;

/**
 * Created by benjaminkc on 17/6/29.
 * 微信 / DIGI 接口控制器
 */
@RestController
@RequestMapping("/api/interface")
public class ApiController {

    @Autowired
    ApiManager apiManager;

    @Autowired
    SourceManager sourceManager;

    @DeleteMapping("delete/all")
    public Response removeAll(){
        Long affect = apiManager.removeAll();
        return Response.ok("affect", affect);
    }

    @ApiOperation(value = "重置所有公众号的所有微信接口到系统默认值")
    @PutMapping("wx/setting/limit/reset/all")
    public Response settingWxApiLimitAll(){
        apiManager.rebuildWxApiLimit(sourceManager.findAllAccounts(),true);
        return Response.ok();
    }

    @ApiOperation(value = "设置指定公众号分派ID下的微信接口限制值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "api", value = "微信接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "设置的日期", required = true, dataType = "String"),
            @ApiImplicitParam(name = "limitValue", value = "限制值", required = true, dataType = "Long")
    })
    @PutMapping("wx/setting/limit")
    public Response settingWxApiLimit(@RequestParam String accountId,
                                      @RequestParam String api,
                                      @RequestParam String date,
                                      @RequestParam Long limitValue){
        Long limit = apiManager.recordWxApiLimit(
                accountId, WxApiType.fromValue(api), LocalDate.parse(date), limitValue);
        return Response.ok("limit", limit);
    }

    @ApiOperation(value = "查看指定公众号分派ID下的微信接口限制值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "api", value = "微信接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "设置的日期", required = true, dataType = "String")
    })
    @GetMapping("wx/call/limit")
    public Response getWxApiLimit(@RequestParam String accountId,
                                      @RequestParam String api,
                                      @RequestParam String date){
        Long limit = apiManager.achieveWxApiLimit(
                accountId, WxApiType.fromValue(api), LocalDate.parse(date));
        boolean isLimit = apiManager.isReachWxApiLimit(
                accountId, WxApiType.fromValue(api), LocalDate.parse(date));
        return Response.ok("limit", limit).add("isLimit", isLimit);
    }

    @ApiOperation(value = "获取所有微信接口")
    @GetMapping("wx")
    public Response getWxApi(){
        EnumSet<WxApiType> typeEnumSet = EnumSet.allOf(WxApiType.class);
        return Response.ok("body",
                typeEnumSet.stream()
                        .map(type -> type.toValue()).toArray(String[]::new));
    }

    @ApiOperation(value = "查看指定公众号分派ID下的微信接口的已调用数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "api", value = "微信接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "设置的日期", required = true, dataType = "String")
    })
    @GetMapping("wx/call/count")
    public Response getWxApiCount(@RequestParam String accountId,
                                  @RequestParam String api,
                                  @RequestParam String date){
        Long count = apiManager.achieveWxApiCount(
                        accountId, WxApiType.fromValue(api),
                        LocalDate.parse(date));
        boolean isLimit = apiManager.isReachWxApiLimit(
                accountId, WxApiType.fromValue(api), LocalDate.parse(date));
        return Response.ok("count", count).add("isLimit", isLimit);
    }

    @ApiOperation(value = "获取所有DIGI接口")
    @GetMapping("digi")
    public Response getDigiApi(){
        EnumSet<DigiApiType> typeEnumSet = EnumSet.allOf(DigiApiType.class);
        return Response.ok("body",
                typeEnumSet.stream()
                        .map(type -> type.toValue()).toArray(String[]::new));
    }

    @ApiOperation(value = "查看指定公众号分派ID下的DIGI接口的已调用数")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "accountId", value = "公众号分派ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "api", value = "微信接口", required = true, dataType = "String"),
            @ApiImplicitParam(name = "date", value = "设置的日期", required = true, dataType = "String")
    })
    @GetMapping("digi/call/count")
    public Response getDigiApiCount(@RequestParam String accountId,
                                  @RequestParam String api,
                                  @RequestParam String date){
        Long count = apiManager.achieveDigiApiCount(
                accountId, DigiApiType.fromValue(api),
                LocalDate.parse(date));
        return Response.ok("count", count);
    }

    @ApiOperation(value = "刷新Open接口缓存")
    @GetMapping("open/refresh")
    public Response refreshOpenAuth(){
        sourceManager.refresh();
        return Response.ok();
    }









    /**
     * 以下接口均面向测试
     * 使用雅姿公众号
     */


    @Value("${id}")
    String id;
    @Value("${name}")
    String name;
    @Value("${site}")
    String site;
    @Value("${sn}")
    String sn;

    @Autowired
    ProxyDigiApi proxyDigiApi;

    @Autowired
    ProxyWxApi proxyWxApi;

    private Account getAccount(String _id, String _name, String _site, String _sn){
        Account account = new Account();
            account.setId(_id);
            account.setName(_name);
            account.setSite(_site);
            account.setSn(_sn);
        return account;
    }

    @GetMapping("digi/achieve/signature")
    public Map achieveSignature(@RequestParam(required = false) String url){
        return proxyDigiApi.callSignature(getAccount(id, name, site, sn),url);
    }

    @GetMapping("digi/achieve/accessToken")
    public Map achieveAccessToken(){
        return proxyDigiApi.callAccessToken(getAccount(id, name, site, sn));
    }

    @GetMapping("digi/achieve/authorize")
    public Map achieveAuthorize(@RequestParam(required = false) String redirect){
        return proxyDigiApi.callAuthorize(getAccount(id, name, site, sn),redirect,true);
    }



    @GetMapping("wx/achieve/articleSummary")
    public Map achieveArticleSummary(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetArticleSummary, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/articleTotal")
    public Map achieveArticleTotal(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetArticleTotal, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userRead")
    public Map achieveUserRead(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserRead, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userReadHour")
    public Map achieveUserReadHour(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserReadHour, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userShare")
    public Map achieveUserShare(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserShare, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userShareHour")
    public Map achieveUserShareHour(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserShareHour, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userCumulate")
    public Map achieveUserCumulate(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserCumulate, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/userSummary")
    public Map achieveUserSummary(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUserSummary, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsg")
    public Map achieveUpStreamMsg(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsg, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgHour")
    public Map achieveUpStreamMsgHour(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgHour, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgWeek")
    public Map achieveUpStreamMsgWeek(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgWeek, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgMonth")
    public Map achieveUpStreamMsgMonth(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgMonth, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgDist")
    public Map achieveUpStreamMsgDist(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgDist, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgDistWeek")
    public Map achieveUpStreamMsgDistWeek(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgDistWeek, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/upStreamMsgDistMonth")
    public Map achieveUpStreamMsgDistMonth(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetUpStreamMsgDistMonth, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/interfaceSummary")
    public Map achieveInterfaceSummary(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetInterfaceSummary, LocalDate.parse(begin), LocalDate.parse(end));
    }

    @GetMapping("wx/achieve/interfaceSummaryHour")
    public Map achieveInterfaceSummaryHour(@RequestParam String begin, @RequestParam String end){
        return proxyWxApi.call(getAccount(id, name, site, sn), WxApiType.GetInterfaceSummaryHour, LocalDate.parse(begin), LocalDate.parse(end));
    }
}
