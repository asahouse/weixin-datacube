package com.andc.amway.datacubecatcher.service;

import com.alibaba.fastjson.JSON;
import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.digi.enumerate.DigiApiType;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.async.DatePair;
import com.andc.amway.datacubecatcher.utils.KCDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by benjaminkc on 17/8/7.
 */
@Slf4j
@Service
public class ApiManager {

    @Autowired
    JedisPool jedisPool;

    @Value("${weixin.datacube.startdate}")
    String weixin_datacube_startdate;

    @Value("${weixin.request-limit}")
    String weixin_request_limit;

    //对开发平台的信息缓存
    public static String API_OPEN_CACHE_TIME_FALG = "expires_in_time";
    public static String API_OPEN_CACHE_INFO_KEY = "datacube:api:open:cache:info:";//HASH [0]->clientId
    public static String API_OPEN_CACHE_APPID_KEY = "datacube:api:open:cache:appid:";//HASH [0]->appId

    //各个接口的调用统计 / 限制
    public static String API_WX_CALL_LIMIT_KEY = "datacube:api:wx:call:limit:";
    public static String API_WX_CALL_COUNT_KEY = "datacube:api:wx:call:count:";
    public static String API_DIGI_CALL_COUNT_KEY = "datacube:api:digi:call:count:";

    //用于计划任务对以往数据进行计划抓取
    public static String API_WX_CALL_DIARY_HISTORY = "datacube:api:wx:call:diary:history:"; //:[0]->aid , :[1]->api
    public static String API_WX_CALL_DIARY_ERROR = "datacube:api:wx:call:diary:error:"; //:[0]->aid , :[1]->api

    public Long removeAll(){
        try(Jedis jedis = jedisPool.getResource()){
            Set<String> allkeys = jedis.keys("datacube:*");
            return jedis.del(allkeys.toArray(new String[]{}));
        }
    }

    public void rebuildWxApiLimit(List<Account> accounts, boolean isCover){
        EnumSet<WxApiType> typeEnumSet = EnumSet.allOf(WxApiType.class);

        LocalDate start = LocalDate.parse(weixin_datacube_startdate);
        Set<LocalDate> dates = KCDateTimeUtils.achieveCalendar(start, LocalDate.now());
        typeEnumSet.parallelStream().forEach(type ->
                accounts.forEach(account ->
                        dates.forEach(d ->{
                            try(Jedis jedis = jedisPool.getResource()){
                                String key = API_WX_CALL_LIMIT_KEY + account.getId() + ":" + type.toValue();
                                if (isCover)
                                    jedis.hsetnx(key, d.toString(), weixin_request_limit);
                                else
                                    jedis.hset(key, d.toString(), weixin_request_limit);
                            }
                        })));
    }

    public Long recordWxApiLimit(String accountId, WxApiType type, LocalDate date, Long limit){
        try(Jedis jedis = jedisPool.getResource()){
            Transaction transaction = jedis.multi();
                transaction.hset(API_WX_CALL_LIMIT_KEY + accountId + ":" + type.toValue(),
                        date.toString(), String.valueOf(limit));
                transaction.hget(API_WX_CALL_LIMIT_KEY + accountId + ":" + type.toValue(),
                        date.toString());
            List<Object> result = transaction.exec();
            if (!result.isEmpty() && Optional.ofNullable(result.get(1)).isPresent())
                return Long.valueOf(result.get(1).toString());
            else
                return 0L;
        }
    }

    public Long recordWxApiCount(String accountId, WxApiType type){
        //累积WX API 调用次数
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.hincrBy(API_WX_CALL_COUNT_KEY + accountId + ":" + type.toValue(),
                    LocalDate.now().toString(), 1);
        }
    }

    public Long recordDigiApiCount(String accountId, DigiApiType type){
        //累积WX API 调用次数
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.hincrBy(API_DIGI_CALL_COUNT_KEY + accountId + ":" + type.toValue(),
                    LocalDate.now().toString(), 1);
        }
    }

    public Long achieveWxApiLimit(String accountId, WxApiType type, LocalDate date){
        try(Jedis jedis = jedisPool.getResource()){
            String result = jedis.hget(
                    API_WX_CALL_LIMIT_KEY + accountId + ":" + type.toValue(),
                    date.toString());
            return StringUtils.isEmpty(result) ? 0L : Long.valueOf(result);
        }
    }

    public Long achieveWxApiCount(String accountId, WxApiType type, LocalDate date){
        try(Jedis jedis = jedisPool.getResource()){
            String result = jedis.hget(
                    API_WX_CALL_COUNT_KEY + accountId + ":" + type.toValue(),
                    date.toString());
            return StringUtils.isEmpty(result) ? 0L : Long.valueOf(result);
        }
    }

    public Long achieveDigiApiCount(String accountId, DigiApiType type, LocalDate date){
        try(Jedis jedis = jedisPool.getResource()){
            String result = jedis.hget(
                    API_DIGI_CALL_COUNT_KEY + accountId + ":" + type.toValue(),
                    date.toString());
            return StringUtils.isEmpty(result) ? 0L : Long.valueOf(result);
        }
    }

    public Boolean isReachWxApiLimit(String accountId, WxApiType type, LocalDate date){
        Long limit = this.achieveWxApiLimit(accountId, type, date);
        Long count = this.achieveWxApiCount(accountId, type, date);
        return count >= limit;
    }

    /**
     * 记录 公众号分派ID下某个微信接口的日计划操作历史日期
     *
     * @param accountId
     * @param type
     * @param start
     * @param end
     * @return
     */
    public Long recordDiaryHistoryDates(String accountId, WxApiType type, LocalDate start, LocalDate end){
        try(Jedis jedis = jedisPool.getResource()){

            Map<String, Double> datas = new HashMap<>();
            KCDateTimeUtils.achieveCalendar(start, end)
                    .stream().forEach(d -> datas.put(
                            d.toString(), Double.valueOf(Long.toString(d.toEpochDay()))));

            return jedis.zadd(API_WX_CALL_DIARY_HISTORY + accountId + ":" + type.toValue(), datas);
        }
    }

    /**
     * 找寻 公众号分派ID下某个微信接口的日计划操作历史日期中的边沿日期
     *
     * @param accountId
     * @param type
     * @param isNear YES->最近日期 , NO->最远日期
     * @return
     */
    public Optional<LocalDate> achieveDiaryHistoryDate(String accountId, WxApiType type, Boolean isNear){
        try(Jedis jedis = jedisPool.getResource()){
            String key = API_WX_CALL_DIARY_HISTORY + accountId + ":" + type.toValue();

            Optional<String> date = isNear ?
                    jedis.zrevrange(key, 0, -1).stream().findFirst() :
                    jedis.zrange(key, 0, -1).stream().findFirst();

            return date.isPresent() ?
                    Optional.ofNullable(LocalDate.parse(date.get())) :
                    Optional.empty();
        }
    }

    /**
     * 记录 公众号分派ID下某个微信接口的日计划操作 的错误信息
     *
     * @param accountId
     * @param type
     * @param pair
     * @param info
     * @return
     */
    public Long recordDiaryError(String accountId, WxApiType type, DatePair pair, String info){
        try(Jedis jedis = jedisPool.getResource()){
            return jedis.hset(API_WX_CALL_DIARY_ERROR + accountId + ":" + type.toValue(),
                    JSON.toJSONString(pair), info);
        }
    }

    /**
     * 对OPEN平台的信息进行过期缓存判定
     * @return
     */
    public Boolean isOverdueOpenCache(String id, String key){
        if (StringUtils.isEmpty(id)) return true;//错误返回已过期

        Boolean result;
        String setkey = key + id;
        try(Jedis jedis = jedisPool.getResource()){
            if (jedis.exists(setkey)){
                String overtime = jedis.hget(setkey, API_OPEN_CACHE_TIME_FALG);//写入数据时,对当下时间追加expires_in时间后的过期时间
                if (StringUtils.isEmpty(overtime)) return true;

                try{
                    //判断时间到期
                    LocalDateTime ldt = LocalDateTime.parse(overtime);
                    result = ldt.isBefore(LocalDateTime.now());//写入的标识时间对比当前时间, 是否已过期

                }catch (Exception ex){ return true; }
            }else result = true;//空则过期,可写入
        }
        return result;
    }

    /**
     * 获取OPEN平台的缓存信息
     * @return
     */
    public Optional<Map<String, Object>> achieveOpenCache(String id, String key){
        if (this.isOverdueOpenCache(id, key)) return Optional.empty(); //已过期便返回空值

        String setkey = key + id;
        try(Jedis jedis = jedisPool.getResource()){
            Map<String, String> result = jedis.hgetAll(setkey);
            if (result.isEmpty()) return Optional.empty();

            return Optional.of(JSON.parseObject(JSON.toJSONString(result), HashMap.class));
        }
    }

    /**
     * 刷新记录OPEN平台的信息到缓存中
     * @param info
     * @return
     */
    public Boolean refreshOpenCache(String id, String key, Map<String, Object> info, Long expires){
        if (info.isEmpty()) return false;

        String setkey = key + id;

        try(Jedis jedis = jedisPool.getResource()){

            //逐个添加
            info.entrySet().stream().forEach(e -> jedis.hset(setkey, e.getKey(), e.getValue().toString()));

            //添加过期时间戳
            if (!info.isEmpty()) {
                LocalDateTime timeFlag = LocalDateTime.now().plusSeconds(expires);
                jedis.hset(setkey, API_OPEN_CACHE_TIME_FALG, timeFlag.toString());
            }
            return true;
        }catch (Exception ex){
            log.error("EX : " + ex.getMessage());
            return false;
        }
    }

}
