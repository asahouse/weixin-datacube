package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.*;
import com.andc.amway.datacubecatcher.persistent.dto.UpStreamMsgDTO;
import com.andc.amway.datacubecatcher.persistent.dto.UpStreamMsgRecordDTO;
import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotalDetail;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsg;
import com.andc.amway.datacubecatcher.persistent.entity.UserCumulate;
import com.andc.amway.datacubecatcher.persistent.entity.UserSummary;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public abstract class AbstractRepositoryManager {

    @Autowired
    ArticleSummaryRepository articleSummaryRepository;

    @Autowired
    ArticleTotalRepository articleTotalRepository;

    @Autowired
    ArticleTotalDetailRepository articleTotalDetailRepository;

    @Autowired
    UserCumulateRepository userCumulateRepository;

    @Autowired
    UserSummaryRepository userSummaryRepository;

    @Autowired
    UserShareRepository userShareRepository;

    @Autowired
    UserShareHourRepository userShareHourRepository;

    @Autowired
    UserReadRepository userReadRepository;

    @Autowired
    UserReadHourRepository userReadHourRepository;

    @Autowired
    CatchEventLogRepository catchEventLogRepository;

    @Autowired
    InterfaceSummaryRepository interfaceSummaryRepository;

    @Autowired
    InterfaceSummaryHourRepository interfaceSummaryHourRepository;

    @Autowired
    UpStreamMsgRepository upStreamMsgRepository;

    @Autowired
    UpStreamMsgHourRepository upStreamMsgHourRepository;

    @Autowired
    UpStreamMsgWeekRepository upStreamMsgWeekRepository;

    @Autowired
    UpStreamMsgMonthRepository upStreamMsgMonthRepository;

    @Autowired
    UpStreamMsgDistRepository upStreamMsgDistRepository;

    @Autowired
    UpStreamMsgDistWeekRepository upStreamMsgDistWeekRepository;

    @Autowired
    UpStreamMsgDistMonthRepository upStreamMsgDistMonthRepository;

    @SneakyThrows
    public BasicDataRepository getRepository(WxApiType type){
        if (WxApiType.GetArticleSummary.equals(type))
            return articleSummaryRepository;
        if (WxApiType.GetArticleTotal.equals(type))
            return articleTotalRepository;
        if (WxApiType.GetUserCumulate.equals(type))
            return userCumulateRepository;
        if (WxApiType.GetUserReadHour.equals(type))
            return userReadHourRepository;
        if (WxApiType.GetUserRead.equals(type))
            return userReadRepository;
        if (WxApiType.GetUserShare.equals(type))
            return userShareRepository;
        if (WxApiType.GetUserShareHour.equals(type))
            return userShareHourRepository;
        if (WxApiType.GetUserSummary.equals(type))
            return userSummaryRepository;
        if (WxApiType.GetInterfaceSummary.equals(type))
            return interfaceSummaryRepository;
        if (WxApiType.GetInterfaceSummaryHour.equals(type))
            return interfaceSummaryHourRepository;
        if (WxApiType.GetUpStreamMsg.equals(type))
            return upStreamMsgRepository;
        if (WxApiType.GetUpStreamMsgHour.equals(type))
            return upStreamMsgHourRepository;
        if (WxApiType.GetUpStreamMsgWeek.equals(type))
            return upStreamMsgWeekRepository;
        if (WxApiType.GetUpStreamMsgMonth.equals(type))
            return upStreamMsgMonthRepository;
        if (WxApiType.GetUpStreamMsgDist.equals(type))
            return upStreamMsgDistRepository;
        if (WxApiType.GetUpStreamMsgDistWeek.equals(type))
            return upStreamMsgDistWeekRepository;
        if (WxApiType.GetUpStreamMsgDistMonth.equals(type))
            return upStreamMsgDistMonthRepository;

        throw new IllegalAccessException("Type does not exist WxApiType Repository!");
    }


    public List<Map<String, Object>> queryFansDataByCollection(List<UserCumulate> collection){
        return collection.parallelStream().map(userCumulate -> {
            Map<String, Object> single = new LinkedHashMap<>();
            //TODO 需要优化逐次查询
            List<UserSummary> details = userSummaryRepository.findByAccountIdAndRefDate(
                    userCumulate.getAccount_id(),
                    userCumulate.getRef_date());
            single.put("ref_date", userCumulate.getRef_date());
            single.put("new_user", details.stream().mapToLong(UserSummary::getNew_user).sum());
            single.put("cancel_user", details.stream().mapToLong(UserSummary::getCancel_user).sum());
            single.put("cumulate_user", userCumulate.getCumulate_user());
            single.put("summary", details);
            single.put("account_id", userCumulate.getAccount_id());
            return single;
        }).collect(Collectors.toList());
    }

    public List<UpStreamMsgDTO> queryFansInteractionByCollection(String accountId, List<Object[]> content) {
        List<UpStreamMsgRecordDTO> sums =
                content.parallelStream().map(UpStreamMsgRecordDTO::new)
                        .collect(Collectors.toList());

        if (sums.isEmpty()) return new ArrayList<>();

        Map<String, List<UpStreamMsg>> rangeTotalMap =
                upStreamMsgRepository.findByAccountIdAndRefDateIn(
                        accountId,
                        sums.parallelStream().map(UpStreamMsgRecordDTO::getRef_date).collect(Collectors.toSet()))
                        .parallelStream().collect(Collectors.groupingBy(UpStreamMsg::getRef_date));

        return sums.parallelStream().map(record -> {
            UpStreamMsgDTO dto = new UpStreamMsgDTO();
            BeanUtils.copyProperties(record, dto);
            dto.setRecords(rangeTotalMap.get(record.getRef_date()));
            return dto;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> queryTuwenPushByCollection(List<ArticleTotalDetail> collection, Map<String, List<UserCumulate>> cumulates){
        return collection.parallelStream().map(articleTotalDetail -> {
            Map<String, Object> single = new LinkedHashMap<>();
            single.put("ref_date", articleTotalDetail.getRef_date());
            single.put("stat_date", articleTotalDetail.getStat_date());
            single.put("title", articleTotalDetail.getTitle());
            single.put("int_page_read_user", articleTotalDetail.getInt_page_read_user());
            single.put("share_user", articleTotalDetail.getShare_user());
            BigDecimal rateShare = new BigDecimal(
                    (float) articleTotalDetail.getShare_user() / (float) articleTotalDetail.getInt_page_read_user() );
            single.put("share_rate", rateShare.doubleValue());

            single.put("target_user", articleTotalDetail.getTarget_user());

            BigDecimal rateOne = new BigDecimal(
                    (float) articleTotalDetail.getInt_page_read_user() / (float) articleTotalDetail.getTarget_user());
            single.put("int_page_read_user_rate", rateOne.doubleValue());

            if (Optional.ofNullable(articleTotalDetail.getRef_date()).isPresent()
                    && Optional.ofNullable(cumulates.get(articleTotalDetail.getRef_date())).isPresent()) {
                Long cumulateUser =
                        cumulates.get(articleTotalDetail.getRef_date())
                                .get(0).getCumulate_user();
                BigDecimal rate = new BigDecimal(
                        (float) articleTotalDetail.getTarget_user() / (float) cumulateUser);
                single.put("target_user_rate", rate.doubleValue());
            }else
                single.put("target_user_rate", 0);
            return single;
        }).collect(Collectors.toList());
    }

}
