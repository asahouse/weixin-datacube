package com.andc.amway.datacubecatcher.utils;

import com.andc.amway.datacubecatcher.async.DatePair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/8/2.
 */
public class KCDateTimeUtils {

    public static Set<LocalDate> achieveCalendar(LocalDate start, LocalDate end){
        //计算历经日历的日期
        Set<LocalDate> calendar = new HashSet<>();
        long duringDays = ChronoUnit.DAYS.between(start, end);
        for (int i = 0; i <= duringDays; i++) {
            LocalDate date = start.plusDays(i);
            calendar.add(date);
        }
        return calendar;
    }

    public static List<DatePair> achieveCalendarPairs(Long range, LocalDate start, LocalDate end){

        //JAVA时间跨度不计算起始日,所以按业务逻辑需要加1,连同本日一并算
        long duringDays = ChronoUnit.DAYS.between(start, end) + 1;
        long r = range < 0 ? 1 : range;

        //计算历经日历的日期
        List<DatePair> calendar = new LinkedList<>();

        //同一天返回即日
        if(start.isEqual(end)) {
            calendar.add(new DatePair(
                    LocalDateTime.of(start, LocalTime.MIN),
                    LocalDateTime.of(end, LocalTime.MAX)));
            return calendar;
        }





        //依据实现类的时间跨度进行划分pair, 同时每个pair
        long dr = duringDays;
        long ra = r;

        long a = dr % ra;//余数
        boolean isSurplus = a > 0;//是否有非整数区间出现
        long b = dr / ra;//正常区间数
        long c = b * ra;//正常区间日数
        long d = dr - c;//非整数的区间日数

        //生产出<计划>
        List<Long> plan = new ArrayList<>();
        for (int i = 0; i < b; i++) { plan.add(ra); }
        if (isSurplus) plan.add(d);

        //按<计划>集合来生产Pair
        Optional<LocalDate> temp = Optional.empty();
        for (int i = 0; i < plan.size(); i++) {
            /**
             * 例子
             *
             * 2017-06-14 -> 2017-06-28
             * 步伐 : 3
             * 经过15日 , 产5个区间
             * 2017-06-14 : 2017-06-16
             * 2017-06-17 : 2017-06-19
             * 2017-06-20 : 2017-06-22
             * 2017-06-23 : 2017-06-25
             * 2017-06-26 : 2017-06-28
             *
             * 每个上区间最后日为下一个区间的开始基数日,到下一个区间时加一日
             * 同时这个区间的结束日减一天,才能符合前增后减符合原步伐日数
             */
            Long step = plan.get(i);
            LocalDate insideStart = temp.isPresent() ? temp.get().plusDays(1) : start;//上次区间最后一日的下一天开始
            LocalDate plusDate = insideStart.plusDays(step-1);//排班需要减一天来呼应开始日加一天
            DatePair pair = new DatePair(
                    LocalDateTime.of(insideStart, LocalTime.MIN),
                    LocalDateTime.of(plusDate, LocalTime.MAX));
            calendar.add(pair);

            temp = Optional.of(plusDate);
        }
        return calendar;
    }

    public static LocalDate[] wipeout(Set<LocalDate> sanctity, Set<LocalDate> filth){
        List<LocalDate> list = new ArrayList(Arrays.asList(new Object[sanctity.size()]));
        Collections.copy(list, new ArrayList<>(sanctity));
        list.removeAll(filth);
        return list.parallelStream().sorted().collect(Collectors.toList()).toArray(new LocalDate[]{});
    }

    public static List<LocalDate[]> healRugged(LocalDate... soul){
        List<Set<LocalDate>> col = new ArrayList<>();
        Set<LocalDate> cd = new LinkedHashSet<>(); // 子集合

        //排序
        soul = Arrays.stream(soul).sorted().collect(Collectors.toList()).toArray(new LocalDate[]{});

        for (int i = 0; i < soul.length; i++) {
            if (i==0){
                //首个
                if (soul.length == 1){
                    //单独
                    Set<LocalDate> temp = new LinkedHashSet<>();
                    temp.add(soul[i]);
                    col.add(temp);
                } else if (soul[i].plusDays(1).equals(soul[i+1])) {
                    //后连贯
                    cd.add(soul[i]);
                    cd.add(soul[i+1]);
                }
            } else if (soul.length - 1 == i){
                //收尾
                col.add(cd);
            } else if (soul[i].plusDays(1).equals(soul[i+1])) {
                //后连贯
                cd.add(soul[i]);
                //连带后连贯日期
                if (soul[i].plusDays(1).equals(soul[i+1])) cd.add(soul[i+1]);

            } else if(!soul[i].plusDays(1).equals(soul[i+1])
                    && !soul[i].minusDays(1).equals(soul[i-1])){
                //前后不连贯的日期
                Set<LocalDate> single = new LinkedHashSet<>();
                single.add(soul[i]);
                col.add(single);
                cd.clear();
                cd.add(soul[i+1]);
            }else if (!soul[i].plusDays(1).equals(soul[i+1])
                    && soul[i].minusDays(1).equals(soul[i-1])){
                //前连贯后不连贯的日期
                if (cd.size() >= 1) {
                    Set<LocalDate> temp = new LinkedHashSet<>(cd);
                    col.add(temp);
                }
                cd.clear();
                cd.add(soul[i+1]);
            }
        }
        return col.parallelStream().map(s -> s.toArray(new LocalDate[]{})).collect(Collectors.toList());
    }

    public static List<DatePair> mercy(List<LocalDate[]> evil){
        return evil.parallelStream().map(e -> new DatePair(e[0], e[e.length-1]))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {
        KCDateTimeUtils.purgatoryOne();
    }

    public static void purgatoryOne(){
        Set<LocalDate> sanctity = new LinkedHashSet<>();
        sanctity.add(LocalDate.parse("2017-08-01"));
        sanctity.add(LocalDate.parse("2017-08-02"));
        sanctity.add(LocalDate.parse("2017-08-03"));
        sanctity.add(LocalDate.parse("2017-08-04"));
        sanctity.add(LocalDate.parse("2017-08-05"));
        sanctity.add(LocalDate.parse("2017-08-06"));
        sanctity.add(LocalDate.parse("2017-08-07"));
        sanctity.add(LocalDate.parse("2017-08-08"));
        sanctity.add(LocalDate.parse("2017-08-09"));
        sanctity.add(LocalDate.parse("2017-08-10"));
        sanctity.add(LocalDate.parse("2017-08-11"));
        sanctity.add(LocalDate.parse("2017-08-12"));
        sanctity.add(LocalDate.parse("2017-08-13"));
        sanctity.add(LocalDate.parse("2017-08-14"));
        sanctity.add(LocalDate.parse("2017-08-15"));
        sanctity.add(LocalDate.parse("2017-08-16"));
        sanctity.add(LocalDate.parse("2017-08-17"));
        sanctity.add(LocalDate.parse("2017-08-18"));
        sanctity.add(LocalDate.parse("2017-08-19"));


        Set<LocalDate> filth = new LinkedHashSet<>();
        //filth.add(LocalDate.parse("2017-08-01"));
        //filth.add(LocalDate.parse("2017-08-02"));
        //filth.add(LocalDate.parse("2017-08-03"));
        //filth.add(LocalDate.parse("2017-08-04"));
        filth.add(LocalDate.parse("2017-08-05"));
        //filth.add(LocalDate.parse("2017-08-06"));
        //filth.add(LocalDate.parse("2017-08-07"));
        filth.add(LocalDate.parse("2017-08-08"));
        filth.add(LocalDate.parse("2017-08-09"));
        //filth.add(LocalDate.parse("2017-08-10"));
        filth.add(LocalDate.parse("2017-08-11"));
        //filth.add(LocalDate.parse("2017-08-12"));
        //filth.add(LocalDate.parse("2017-08-13"));
        //filth.add(LocalDate.parse("2017-08-14"));
        filth.add(LocalDate.parse("2017-08-15"));
        filth.add(LocalDate.parse("2017-08-16"));
        filth.add(LocalDate.parse("2017-08-17"));
        //filth.add(LocalDate.parse("2017-08-18"));
        filth.add(LocalDate.parse("2017-08-19"));

        LocalDate[] soul = KCDateTimeUtils.wipeout(sanctity, filth);
        List<LocalDate[]> evil = KCDateTimeUtils.healRugged(soul);
        System.out.println(KCDateTimeUtils.mercy(evil));
    }

    public static void purgatory(){
        LocalDate[] nsd = {
                LocalDate.parse("2017-08-01"),
                //LocalDate.parse("2017-08-02"),
                //LocalDate.parse("2017-08-03"),
                LocalDate.parse("2017-08-04"),
                LocalDate.parse("2017-08-05"),
                LocalDate.parse("2017-08-06"),
                //LocalDate.parse("2017-08-07"),
                LocalDate.parse("2017-08-08"),
                LocalDate.parse("2017-08-09"),
                LocalDate.parse("2017-08-10"),
                //LocalDate.parse("2017-08-11"),
                LocalDate.parse("2017-08-12"),
                //LocalDate.parse("2017-08-13"),
                //LocalDate.parse("2017-08-14"),
                LocalDate.parse("2017-08-15"),
                //LocalDate.parse("2017-08-16"),
                LocalDate.parse("2017-08-17"),
                LocalDate.parse("2017-08-18"),
                LocalDate.parse("2017-08-19"),
                //LocalDate.parse("2017-08-20"),
                LocalDate.parse("2017-08-21"),
                LocalDate.parse("2017-08-22"),
                };

        System.out.println(KCDateTimeUtils.healRugged(nsd));
    }
}
