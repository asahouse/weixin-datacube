package com.andc.amway.datacubecatcher.wx.api;

import com.andc.amway.datacubecatcher.wx.Protocol;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/**
 * Created by benjaminkc on 17/6/30.
 */
public class WxConnector {

    public static String getApiPath(Protocol protocol, WxApiHost host, WxApiInterface inter, WxApiType apiType){
        StringBuffer sb = new StringBuffer(protocol.toValue());
        return sb.append(host.toValue())
                .append(inter.toValue())
                .append(apiType.toValue()).toString();
    }

    public static String getTemplateParametersString(String accessToken, Map<String, String> var){
        Map<String, String> parmMap = WxConnector.getTemplateParameters(accessToken, var);
        String combine = WxConnector.getCombineURLParameter(parmMap);
        return combine;
    }

    public static Map<String, String> getTemplateParameters(String accessToken, Map<String, String> var){

        Map<String,String> parmMap = new HashMap<>();
            parmMap.put("access_token",accessToken);

        if (Optional.ofNullable(var).isPresent() && !var.isEmpty())
            parmMap.putAll(var);

        return parmMap;
    }

    public static String getCombineURLParameter(Map<String, String> params) {
        Map<String, String> sortMap = new TreeMap<String, String>();
        sortMap.putAll(params);
        // 以k1=v1&k2=v2...方式拼接参数
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> s : sortMap.entrySet()) {
            String k = s.getKey();
            String v = s.getValue();
            if (StringUtils.isEmpty(v)) {// 过滤空值
                continue;
            }
            builder.append(k).append("=").append(v).append("&");
        }
        if (!sortMap.isEmpty()) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

}
