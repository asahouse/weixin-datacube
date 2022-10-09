package com.andc.amway.datacubecatcher.wx.api;

import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.wx.Protocol;
import com.andc.amway.datacubecatcher.service.ApiManager;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjaminkc on 17/6/30.
 */
@Slf4j
@Component
public class ProxyWxApi {

    @Autowired
    SourceManager sourceManager;//隔离底层数据来源

    @Autowired
    ApiManager apiManager;

    public Map<String, Object> call(Account account, WxApiType type,
                                    LocalDate beginDate, LocalDate endDate){
        Map<String, String> paraMap = new HashMap<>();
            paraMap.put("begin_date", beginDate.toString());
            paraMap.put("end_date", endDate.toString());
        return this.call(account, type, paraMap);
    }

    private Map<String, Object> call(Account account, WxApiType type, Map<String, String> var) {
        String accessToken = sourceManager.callAccessToken(account);
        String apiPath = WxConnector.getApiPath(
                Protocol.https, WxApiHost.official, WxApiInterface.datacube, type);

        Map<String, String> query = new HashMap<>();
            query.put("access_token", accessToken);

        //累积WX API 调用次数
        apiManager.recordWxApiCount(account.getId(), type);

        log.info("API : "+apiPath+" , QUERY : "+query+" , VAR : "+JSONObject.toJSONString(var));
        String returnValue = HttpKit.post(apiPath, query, JSONObject.toJSONString(var), null);
        return JSONObject.parseObject(returnValue, HashMap.class);
    }

}
