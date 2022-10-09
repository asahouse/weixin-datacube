package com.andc.amway.datacubecatcher.wx.source.digi;

import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiApiType;
import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiSiteType;
import com.andc.amway.datacubecatcher.wx.Protocol;
import com.jfinal.kit.HashKit;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * Created by benjaminkc on 17/6/29.
 */
public class DigiApiConnector {

    public static String getApiPath(Protocol protocol, DigiSiteType siteType, DigiApiType apiType){
        StringBuffer sb = new StringBuffer(protocol.toValue());
        return sb.append(siteType.toValue()).append(apiType.toValue()).toString();
    }

    public static String getTemplateParametersString(String id, String sn, Map<String, String> var){
        Map<String, String> parmMap = DigiApiConnector.getTemplateParameters(id, sn, var);
        String combine = DigiApiConnector.getCombineURLParameter(parmMap);
        return combine;
    }

    public static Map<String, String> getTemplateParameters(String id, String sn, Map<String, String> var){
        String nonce_str = DigiApiConnector.getRandomAtSize(32);

        String timestamp = String.valueOf(System.currentTimeMillis())
                .toString().substring(0,10);

        Map<String,String> parmMap = new HashMap<>();
            parmMap.put("id",id);
            parmMap.put("nonce_str",nonce_str);
            parmMap.put("timestamp", timestamp);

        if (Optional.ofNullable(var).isPresent() && !var.isEmpty())
            parmMap.putAll(var);

        String md5Str = "id="+id+"&nonce_str="+nonce_str+"&timestamp="+timestamp+"&sn="+sn;
            parmMap.put("sign", HashKit.md5(md5Str).toUpperCase());

        return parmMap;
    }

    private static String getRandomAtSize(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    private static String getCombineURLParameter(Map<String, String> params) {
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
