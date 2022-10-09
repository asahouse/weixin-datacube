package com.andc.amway.datacubecatcher.service;

import com.andc.amway.datacubecatcher.persistent.dto.*;
import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotalDetail;
import com.andc.amway.datacubecatcher.persistent.entity.UserCumulate;
import com.andc.amway.datacubecatcher.persistent.entity.UserSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DownloadManager extends AbstractRepositoryManager {

    public List<DownloadAtFansDataDTO> downloadAtFansData(String accountId,
                                                          String start,
                                                          String end){
        List<UserCumulate> content =
                userCumulateRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end);

        List<Map<String, Object>> map = this.queryFansDataByCollection(content);

        List<DownloadAtFansDataDTO> result = map.parallelStream().map(e -> {
                DownloadAtFansDataDTO dto = new DownloadAtFansDataDTO();
                e.keySet().parallelStream().forEach(key -> {
                    if ("ref_date".equals(key)) dto.setRef_date(e.get(key).toString());
                    if ("account_id".equals(key)) dto.setAccount_id(e.get(key).toString());
                    if ("new_user".equals(key)) dto.setNew_user((Long) e.get(key));
                    if ("cancel_user".equals(key)) dto.setCancel_user((Long) e.get(key));
                    if ("cumulate_user".equals(key)) dto.setCumulate_user((Long) e.get(key));

                    if ("summary".equals(key)) {
                        List<UserSummary> details = (List<UserSummary>) e.get(key);
                        details.forEach(userSummary -> {
                            switch (userSummary.getUser_source()) {
                                case 0: {//其他合计
                                    dto.setNew_qthj(userSummary.getNew_user());
                                    dto.setCancel_qthj(userSummary.getCancel_user());
                                    break;
                                }
                                case 1: {//公众号搜索
                                    dto.setNew_gzhss(userSummary.getNew_user());
                                    dto.setCancel_gzhss(userSummary.getCancel_user());
                                    break;
                                }
                                case 17: {//名片分享
                                    dto.setNew_mpfs(userSummary.getNew_user());
                                    dto.setCancel_mpfs(userSummary.getCancel_user());
                                    break;
                                }
                                case 30: {//扫描二维码
                                    dto.setNew_smewm(userSummary.getNew_user());
                                    dto.setCancel_smewm(userSummary.getCancel_user());
                                    break;
                                }
                                case 43: {//图文页右上角分享
                                    dto.setNew_twyysjcd(userSummary.getNew_user());
                                    dto.setCancel_twyysjcd(userSummary.getCancel_user());
                                    break;
                                }
                                case 51: {//支付后关注(支付完成页)
                                    dto.setNew_zfhgz(userSummary.getNew_user());
                                    dto.setCancel_zfhgz(userSummary.getCancel_user());
                                    break;
                                }
                                case 57: {//图文页内公众号名称
                                    dto.setNew_twyngzhmc(userSummary.getNew_user());
                                    dto.setCancel_twyngzhmc(userSummary.getCancel_user());
                                    break;
                                }
                                case 75: {//公众号文章广告
                                    dto.setNew_gzhwzgg(userSummary.getNew_user());
                                    dto.setCancel_gzhwzgg(userSummary.getCancel_user());
                                    break;
                                }
                                case 78: {//朋友圈广告
                                    dto.setNew_pyqgg(userSummary.getNew_user());
                                    dto.setCancel_pyqgg(userSummary.getCancel_user());
                                    break;
                                }
                            }
                        });
                    }
                });
                return dto;
            }
        ).sorted(Comparator.comparing(DownloadAtFansDataDTO::getRef_date).reversed()).collect(Collectors.toList());

        return result;
    }

    public List<DownloadAtFansInteractionDTO> downloadAtFansInteraction(String accountId,
                                                                        String start,
                                                                        String end){
        List<Object[]> content =
                upStreamMsgRepository.findRecordsByAccountIdAndRefDateBetween(
                       accountId, start, end);

        List<UpStreamMsgDTO> dtos = this.queryFansInteractionByCollection(accountId, content);

        List<DownloadAtFansInteractionDTO> result = dtos.parallelStream().map(dto -> {
            DownloadAtFansInteractionDTO download = new DownloadAtFansInteractionDTO();
            BeanUtils.copyProperties(dto, download);
            dto.getRecords().stream().forEach(usm -> {
                switch (usm.getMsg_type()){
                    case 1:{//文字
                        download.setMsg_user_wz(usm.getMsg_user());
                        download.setMsg_count_wz(usm.getMsg_count());
                        break;
                    }
                    case 2:{//图片
                        download.setMsg_user_tp(usm.getMsg_user());
                        download.setMsg_count_tp(usm.getMsg_count());
                        break;
                    }
                    case 3:{//语音
                        download.setMsg_user_yy(usm.getMsg_user());
                        download.setMsg_count_yy(usm.getMsg_count());
                        break;
                    }
                    case 4:{//视频
                        download.setMsg_user_sp(usm.getMsg_user());
                        download.setMsg_count_sp(usm.getMsg_count());
                        break;
                    }
                    case 6:{//第三方应用消息
                        download.setMsg_user_dsfyy(usm.getMsg_user());
                        download.setMsg_count_dsfyy(usm.getMsg_count());
                        break;
                    }
                }
            });
            return download;
        }).collect(Collectors.toList());
        return result;
    }

    public List<DownloadAtTuwenPushDTO> downloadAtTuwenPush(String accountId,
                                                            String start,
                                                            String end){
        List<ArticleTotalDetail> content =
                articleTotalDetailRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end);

        Map<String, List<UserCumulate>> cumulates =
                userCumulateRepository.findByAccountIdAndRefDateBetween(
                        accountId, start, end)
                        .parallelStream()
                        .collect(Collectors.groupingBy(UserCumulate::getRef_date));

        List<Map<String, Object>> map = this.queryTuwenPushByCollection(content, cumulates);

        List<DownloadAtTuwenPushDTO> result = map.parallelStream().map(e -> {
                    DownloadAtTuwenPushDTO dto = new DownloadAtTuwenPushDTO();
                    e.keySet().parallelStream().forEach(key -> {
                        if ("ref_date".equals(key)) dto.setRef_date(e.get(key).toString());
                        if ("stat_date".equals(key)) dto.setStat_date(e.get(key).toString());
                        if ("account_id".equals(key)) dto.setAccount_id(e.get(key).toString());
                        if ("title".equals(key)) dto.setTitle(e.get(key).toString());
                        if ("int_page_read_user".equals(key)) dto.setInt_page_read_user((long) e.get(key));
                        if ("int_page_read_user_rate".equals(key)) dto.setInt_page_read_user_rate(Double.valueOf(e.get(key).toString()));
                        if ("target_user".equals(key)) dto.setTarget_user((long) e.get(key));
                        if ("target_user_rate".equals(key)) dto.setTarget_user_rate(Double.valueOf(e.get(key).toString()));
                        if ("share_user".equals(key)) dto.setShare_user((long) e.get(key));
                        if ("share_rate".equals(key)) dto.setShare_rate(Double.valueOf(e.get(key).toString()));
                    });
                    return dto;
                }
        ).sorted(Comparator.comparing(DownloadAtTuwenPushDTO::getRef_date).reversed()).collect(Collectors.toList());

        return result;
    }

    public List<DownloadAtTuwenReadShareAddDTO> downloadAtTuwenReadAndShareAndAdd(String accountId,
                                                                                  String start,
                                                                                  String end){
        List<ArticleTotalDetail> content = articleTotalDetailRepository.findByAccountIdAndRefDateBetween(
                accountId, start, end);
        return content.parallelStream().map(atd -> {
            DownloadAtTuwenReadShareAddDTO dto = new DownloadAtTuwenReadShareAddDTO();
            BeanUtils.copyProperties(atd, dto);
            BigDecimal rateOne = new BigDecimal(
                    (float) dto.getShare_user() / (float) dto.getInt_page_read_user() );
            dto.setShare_rate(rateOne.doubleValue());
            return dto;
        }).collect(Collectors.toList());
    }


}
