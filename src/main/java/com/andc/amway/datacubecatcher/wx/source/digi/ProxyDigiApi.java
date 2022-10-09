package com.andc.amway.datacubecatcher.wx.source.digi;

import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiApiType;
import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiSiteType;
import com.andc.amway.datacubecatcher.wx.Protocol;
import com.andc.amway.datacubecatcher.service.ApiManager;
import com.jfinal.kit.HttpKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by benjaminkc on 17/6/29.
 */
@Slf4j
@Component
public class ProxyDigiApi {

    @Autowired
    ApiManager apiManager;

    public Map<String, Object> callAccessToken(Account account) {
        return this.call(account.getId(), account.getSite(), account.getSn(), null, DigiApiType.access_token);
    }

    public Map<String, Object> callSignature(Account account, String url) {
        Map<String,String> parmMap = new HashMap<>();
            parmMap.put("url",url);
        return this.call(account.getId(), account.getSite(), account.getSn(), parmMap, DigiApiType.signature);
    }

    public Map<String, Object> callAuthorize(Account account, String redirect, boolean isBase) {
        Map<String,String> parmMap = new HashMap<>();
            parmMap.put("scope",isBase?"snsapi_base":"snsapi_userinfo");
            parmMap.put("redirect",redirect);
        return this.call(account.getId(), account.getSite(), account.getSn(), parmMap, DigiApiType.authorize);
    }

    private Map<String, Object> call(String id, String site, String sn, Map<String, String> var, DigiApiType type) {
        String apiPath = DigiApiConnector.getApiPath(Protocol.http, DigiSiteType.fromSite(site), type);
        String parameter = DigiApiConnector.getTemplateParametersString(
                id, sn, var);

        //累积WX API 调用次数
        apiManager.recordDigiApiCount(id, type);

        log.info("API : "+apiPath+" , PARAMETER : "+parameter+" , VAR : "+JSONObject.toJSONString(var));
        String returnValue = HttpKit.post(apiPath, parameter);
        return JSONObject.parseObject(returnValue, HashMap.class);
    }

}
