package com.andc.amway.datacubecatcher.wx.source.open;

import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.Protocol;
import com.andc.amway.datacubecatcher.wx.source.open.enumerate.OpenApiType;
import com.andc.amway.datacubecatcher.wx.source.open.enumerate.OpenAuthType;
import com.andc.amway.datacubecatcher.service.ApiManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Component
public class ProxyOpenApi {

    @Value("${source.open.clientId}")
    private String clientId;
    @Value("${source.open.secret}")
    private String secret;
    @Value("${source.open.path}")
    private String path;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApiManager apiManager;

    public Map<String, Object> callAccessToken(String appId) {
        Optional<Map<String, Object>> infoOptional =
                apiManager.achieveOpenCache(appId, ApiManager.API_OPEN_CACHE_APPID_KEY);
        if (infoOptional.isPresent()) return infoOptional.get();

        Map<String, Object> result = this.call(appId, OpenApiType.token);
        try{
            if (result.isEmpty()) throw new IllegalStateException("Call AppId Access Token Empty!!");

            if (!Optional.ofNullable(result.get("expireInSeconds")).isPresent()
                    || !Optional.ofNullable(result.get("systemCurrentMillis")).isPresent()){
                log.error("expireInSeconds : "+result.get("expireInSeconds"));
                log.error("systemCurrentMillis : "+result.get("systemCurrentMillis"));
                throw new IllegalArgumentException("Call AppId expire properties is empty!");
            }

            Long openExpireMills = Long.valueOf(result.get("expireInSeconds").toString());
            Long openMills = Long.valueOf(result.get("systemCurrentMillis").toString());
            LocalDateTime openMillsTime = new Date(openMills).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDateTime();

            LocalDateTime openOverTime = openMillsTime.plusSeconds(openExpireMills);

            if (openOverTime.isBefore(LocalDateTime.now()))
                throw new IllegalArgumentException("Open AppId Time-Expired!!");

            Long diff = ChronoUnit.SECONDS.between(LocalDateTime.now(), openOverTime);

            Boolean isOkCache =
                    apiManager.refreshOpenCache(appId, ApiManager.API_OPEN_CACHE_APPID_KEY,
                            result, diff);//刷新缓存
            log.info("Cache Status -> " + isOkCache);
        }catch (Exception ex){
            log.error(ex.getMessage());
        }
        return result;
    }

    public Map<String, Object> callDetail(String appId) {
        return this.call(appId, OpenApiType.detail);
    }

    /**
     * 获取本系统对第三方平台的授权信息
     * @return
     */
    public Map<String, Object> getOpenAuthInfoCache(){
        //返回缓存
        Optional<Map<String, Object>> infoOptional =
                apiManager.achieveOpenCache(clientId, ApiManager.API_OPEN_CACHE_INFO_KEY);
        if (infoOptional.isPresent()){
            return infoOptional.get();
        }
        return this.getOpenAuthInfo();
    }

    /**
     * 从新获取并刷新缓存
     * @return
     */
    @SneakyThrows
    public Map<String, Object> getOpenAuthInfo(){
        //返回其他OpenApi的Authorization 信息
        String base64 = Base64.getEncoder()
                .encodeToString(new String(clientId +":"+ secret)
                        .getBytes("UTF-8"));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic "+ base64);
        headers.set("Accept", MediaType.APPLICATION_JSON.toString());

        String url = OpenApiConnector.getAuthPath(Protocol.http, path, OpenAuthType.oauth_client);
        String result = new String();
        Map<String, Object> returnValue = new HashMap<>();
        try{
            result = restTemplate.postForObject(url, new HttpEntity<String>(headers), String.class);//字符串返回
            String error = JSONObject.parseObject(result).getString("error");
            if (StringUtils.isEmpty(result)){
                throw new IllegalArgumentException("Open API Auth Request IS EMPTY!!");
            }
            if (!StringUtils.isEmpty(error)){
                throw new IllegalArgumentException("Open API Auth Request Return Error!!");
            }
            returnValue = JSONObject.parseObject(result, HashMap.class);


            Boolean isOkCache =
                    apiManager.refreshOpenCache(clientId, ApiManager.API_OPEN_CACHE_INFO_KEY,
                    returnValue,
                    Long.valueOf(returnValue.get("expires_in").toString()));//刷新缓存

            log.info("Cache Status -> " + isOkCache);

        }catch (RuntimeException ex){
            log.error("=== Open API Auth Request Error ===");
            log.error("clientId : " + clientId);
            log.error("secret : " + secret);
            log.error("url : " + url);
            log.error("result : " + result);
            log.error("headers : " + headers);
            log.error("ex : " + ex.getMessage());
            log.error("=== Open API Auth Request Error end===");
        }finally {
            return returnValue;
        }
    }

    public String getOpenAccessToken(){
        Map<String ,Object> info = this.getOpenAuthInfoCache();
        String openAccessToken = info.get("access_token").toString();
        String openType = info.get("token_type").toString();

        String authorization = openType + " " + openAccessToken;
        log.info("Open API Info -> "+authorization);
        return authorization;
    }

    private Map<String, Object> call(String appId, OpenApiType type) {

        String authorization = this.getOpenAccessToken();//通过Open平台获取单个公众号的授权信息再去请求公众号的api
        String apiPath = OpenApiConnector.getApiPath(Protocol.http, path, type, appId);//将会把id嵌套到api路径上

        log.info("API : "+apiPath+" , AUTHORIZATION : "+JSONObject.toJSONString(authorization));

        HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authorization);
            headers.set("Accept", MediaType.APPLICATION_JSON.toString());
            headers.setContentType(MediaType.APPLICATION_JSON);

        String returnValue = new String();
        try{
            ResponseEntity<String> response = restTemplate.exchange(apiPath,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    String.class);
            returnValue = response.getBody();
            String error = JSONObject.parseObject(returnValue).getString("error");
            if (!StringUtils.isEmpty(error)){
                throw new IllegalArgumentException("Open API Request Return Error!!");
            }
        }catch (RuntimeException ex){
            log.error("=== Open API Request Error ===");
            log.error("clientId : " + clientId);
            log.error("secret : " + secret);
            log.error("apiPath : " + apiPath);
            log.error("authorization : " + authorization);
            log.error("returnValue : " + returnValue);
            log.error("ex : " + ex.toString());
            log.error("=== Open API Request Error end===");
        }
        if(StringUtils.isEmpty(returnValue)) return new HashMap<>();
        return JSONObject.parseObject(returnValue, HashMap.class);
    }
}
