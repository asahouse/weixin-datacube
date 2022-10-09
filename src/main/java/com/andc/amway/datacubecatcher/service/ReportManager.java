package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dto.*;
import com.andc.amway.datacubecatcher.persistent.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ReportManager extends AbstractRepositoryManager {

    public Page<AbstractEntity> report(String api,
                                       String accountId,
                                       String start,
                                       String end,
                                       Pageable page){
        return this.getRepository(WxApiType.fromValue(api))
                        .findByAccountIdAndRefDateBetween(accountId, start, end, page);
    }

    public List<ArticleTotalDetailDiffDTO> diff(Integer day){
        return articleTotalDetailRepository.findObjectsAtRefDatasByDiffDay(day)
                        .parallelStream().filter(o -> Optional.ofNullable(o[0]).isPresent())
                        .map(ArticleTotalDetailDiffDTO::new).collect(Collectors.toList());
    }

    public Map<String, Long> trendAtFans(String accountId,
                                         String start,
                                         String end){
        Map<String, Long> result = new ConcurrentHashMap<>();
        userCumulateRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end)
                .parallelStream().forEach(userCumulate ->
                result.put(userCumulate.getRef_date(), userCumulate.getCumulate_user()));
        return result;
    }

    public Map<String, Long> trendAtRead(String accountId,
                                         String start,
                                         String end){
        Map<String, Long> result = new ConcurrentHashMap<>();
        articleSummaryRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end)
                .parallelStream().forEach(articleSummary ->
                result.put(articleSummary.getRef_date(), articleSummary.getInt_page_read_count()));
        return result;
    }

    public Map<String, Long> trendAtAdd(String accountId,
                                        String start,
                                        String end){
        Map<String, Long> result = new ConcurrentHashMap<>();
        articleSummaryRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end)
                .parallelStream().forEach(articleSummary ->
                result.put(articleSummary.getRef_date(), articleSummary.getAdd_to_fav_count()));
        return result;
    }

    public Map<String, Long> trendAtShare(String accountId,
                                          String start,
                                          String end){
        Map<String, Long> result = new ConcurrentHashMap<>();
        articleSummaryRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end)
                .parallelStream().forEach(articleSummary ->
                result.put(articleSummary.getRef_date(), articleSummary.getShare_count()));
        return result;
    }

    public Page<Map<String, Object>> reportAtFansData(String accountId,
                                                      String start,
                                                      String end,
                                                      Pageable pageable){
        Page<UserCumulate> pages =
                userCumulateRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end, pageable);

        List<Map<String, Object>> collection = this.queryFansDataByCollection(pages.getContent());

        Page<Map<String, Object>> result = new PageImpl<>(collection, pageable, pages.getTotalElements());
        return result;
    }

    public Page<UpStreamMsgDTO> reportAtFansInteraction(String accountId,
                                                              String start,
                                                              String end,
                                                              Pageable pageable){
        Page<Object[]> pages =
                upStreamMsgRepository.findRecordsByAccountIdAndRefDateBetween(
                        accountId, start, end, pageable);

        List<UpStreamMsgDTO> dtos = this.queryFansInteractionByCollection(accountId, pages.getContent());

        Page<UpStreamMsgDTO> result = new PageImpl<>(dtos, pageable, pages.getTotalElements());

        return result;
    }

    public Page<Map<String, Object>> reportAtTuwenPush(String accountId,
                                                       String start,
                                                       String end,
                                                       Pageable pageable){
        Page<ArticleTotalDetail> pages =
                articleTotalDetailRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end, pageable);

        Map<String, List<UserCumulate>> cumulates =
                userCumulateRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end)
                        .parallelStream()
                        .collect(Collectors.groupingBy(UserCumulate::getRef_date));

        List<Map<String, Object>> collection = this.queryTuwenPushByCollection(pages.getContent(), cumulates);

        Page<Map<String, Object>> result = new PageImpl<>(collection, pageable, pages.getTotalElements());
        return result;
    }

    public Page<ArticleTotalDetailDTO> reportAtTuwenReadAndShareAndAdd(String accountId,
                                                                       String start,
                                                                       String end,
                                                                       Pageable pageable){
        Page<ArticleTotalDetail> result = articleTotalDetailRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end, pageable);
        return result.map(atd -> {
            ArticleTotalDetailDTO dto = new ArticleTotalDetailDTO();
            BeanUtils.copyProperties(atd, dto);
            BigDecimal rateOne = new BigDecimal(
                    (float) dto.getShare_user() / (float) dto.getInt_page_read_user() );
            dto.setShare_rate(rateOne.doubleValue());
            return dto;
        });
    }

    public List<ArticleTotalDetailStatisticsDTO> statisticsForTuwenReadAndShareAndAdd(String accountId,
                                                                                      String start,
                                                                                      String end){
        List<Object[]> result =
                articleTotalDetailRepository.statistics(accountId, start, end);

        return result.parallelStream().map(ArticleTotalDetailStatisticsDTO::new).collect(Collectors.toList());
    }

    public Page<CatchEventLog> reportAtCatchEventLog(String accountId, String start, String end, Pageable pageable){
        return catchEventLogRepository.findByCreateTimeBetween(
                accountId,
                LocalDateTime.of(LocalDate.parse(start), LocalTime.MIN),
                LocalDateTime.of(LocalDate.parse(end), LocalTime.MAX),
                pageable);
    }
}
