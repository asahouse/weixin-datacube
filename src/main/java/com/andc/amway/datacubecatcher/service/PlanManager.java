package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.utils.KCDateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/8/2.
 * 接口,调用次数,数据日历 的控制器
 */
@Slf4j
@Service
public class PlanManager {

    @Autowired
    JedisPool jedisPool;

    @Autowired
    SourceManager sourceManager;

    @Value("${weixin.datacube.startdate}")
    String weixin_datacube_startdate;

    public static String DATACUBE_PLAN_CALENDAR = "datacube:plan:calendar";
    public static String DATACUBE_PLAN_DELIMIT = "datacube:plan:delimit:"; // :[0]->公众号分派ID :[1]->接口地址

    public enum PlanStatus {
        NONE(0),//未操作
        VISITED(1),//已访问
        CATCH(2);//已访问并已抓取

        private Integer value;

        PlanStatus(Integer value){
            this.value = value;
        }

        public static PlanStatus fromValue(Integer value){
            if (Optional.ofNullable(value).isPresent()) {
                for (PlanStatus type : values()) {
                    if (type.value.equals(value)) {
                        return type;
                    }
                }
            }
            return null;
        }

        public Integer toValue() {
            return value;
        }
    }


    /**
     * 查询单个公众号分派ID下, 指定微信接口的日期范围内的数据状态
     * @param accountId
     * @param type
     * @param start
     * @param end
     * @return
     */
    public List<Map.Entry<String, String>> find(String accountId, WxApiType type,
                                    LocalDate start, LocalDate end){
        Set<LocalDate> datas = KCDateTimeUtils.achieveCalendar(start, end);
        List<Map.Entry<String, String>> resultJedis =
                this.scanHSet(DATACUBE_PLAN_DELIMIT + accountId + ":" + type.toValue(), datas);
        return resultJedis;
    }

    /**
     * 产生历史日历
     * @return
     */
    public List<Object> rebuildPlanCalendar(){
        LocalDate start = LocalDate.parse(weixin_datacube_startdate);

        try(Jedis jedis = jedisPool.getResource()){
            Transaction transaction = jedis.multi();

            Map<String, Double> datas = new HashMap<>();
            KCDateTimeUtils.achieveCalendar(start, LocalDate.now())
                    .stream().forEach(d -> datas.put(d.toString(), Double.valueOf(Long.toString(d.toEpochDay()))));

            transaction.zadd(DATACUBE_PLAN_CALENDAR, datas);
            return transaction.exec();
        }
    }

    /**
     * 产生所有公众号的所有数据接口的日历
     */
    public void rebuildPlanWxApiCalendar(){
        EnumSet<WxApiType> typeEnumSet = EnumSet.allOf(WxApiType.class);
        List<Account> accounts = sourceManager.findAllAccounts();

        LocalDate start = LocalDate.parse(weixin_datacube_startdate);
        Set<LocalDate> dates = KCDateTimeUtils.achieveCalendar(start, LocalDate.now());
        typeEnumSet.parallelStream().forEach(type ->
                accounts.forEach(account ->
                        dates.forEach(d ->{
                                try(Jedis jedis = jedisPool.getResource()){
                                    jedis.hsetnx(DATACUBE_PLAN_DELIMIT + account.getId() + ":" + type.toValue(),
                                            d.toString(),
                                            PlanStatus.NONE.toValue().toString());

                                }
                        })));
    }

    /***
     * 把微信接口对某日的数据进行登记, 表示该数据已获取到DB上
     *
     * @param type 微信接口类型
     * @param dates 记录的日期集合
     * @param planStatus 记录的类型
     * @return
     */
    public List<Long> recordPlanDataStatusByWxApi(Account account, WxApiType type, Set<String> dates, PlanStatus planStatus){
        if (dates.isEmpty()) return new ArrayList<>();

        try(Jedis jedis = jedisPool.getResource()){
            return dates.stream().map(d ->
                    jedis.hset(DATACUBE_PLAN_DELIMIT + account.getId() + ":" +type.toValue(), d,
                            planStatus.toValue().toString()))
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取 微信接口对某日的数据状态
     * @param account
     * @param type
     * @param date
     * @return
     */
    public PlanStatus achievePlanDataStatusByWxApi(Account account, WxApiType type, LocalDate date){
        try(Jedis jedis = jedisPool.getResource()){
            String key = DATACUBE_PLAN_DELIMIT + account.getId() + ":" +type.toValue();
            String result = jedis.hget(key, date.toString());
            return PlanStatus.fromValue(Integer.valueOf(result));
        }
    }

    /***
     * 获取 微信接口对多个日期的数据状态
     * @param account
     * @param type
     * @param dates
     * @return
     */
    public Map<LocalDate, PlanStatus> achievePlanDatasStatusByWxApi(Account account, WxApiType type, Set<LocalDate> dates){
        try(Jedis jedis = jedisPool.getResource()){
            Transaction transaction = jedis.multi();
            Map<LocalDate, PlanStatus> result = new HashMap<>();
            Map<LocalDate, Response<String>> temp = new HashMap<>();
            dates.parallelStream().forEach(d -> {
                String key = DATACUBE_PLAN_DELIMIT + account.getId() + ":" +type.toValue();
                Response<String> resp = transaction.hget(key, d.toString());
                temp.put(d, resp);
            });
            transaction.exec();
            temp.entrySet().parallelStream()
                    .forEach(e -> {
                        if (Optional.ofNullable(e.getValue().get()).isPresent()) {
                            result.put(e.getKey(),
                                    PlanStatus.fromValue(Integer.valueOf(e.getValue().get())));
                        }else log.warn("Plan Data not exist -> "+e.getKey());
                    });

            return result;
        }
    }


    /**
     * 全局扫描hset
     *
     * @param matchs field匹配模式
     * @return
     */
    private List<Map.Entry<String, String>> scanHSet(String key, Set<LocalDate> matchs) {
        try (Jedis jedis = jedisPool.getResource()){

            List<Map.Entry<String, String>> list = new ArrayList<>();

            matchs.forEach(match -> {
                int cursor = 0;
                //扫描条件 , 星号为多位替代符
                ScanParams scanParams = new ScanParams();
                    scanParams.match(match.toString());
                ScanResult<Map.Entry<String, String>> scanResult;
                do {
                    scanResult = jedis.hscan(key, String.valueOf(cursor), scanParams);
                    list.addAll(scanResult.getResult());//每次迭代的结果
                    cursor = Integer.parseInt(scanResult.getStringCursor());//下级的字符游标
                } while (cursor > 0);
            });

            return list;

        } catch (Exception ex) {
            log.error("scanHSet error : ", ex);
        }

        return new ArrayList<>();
    }
}
