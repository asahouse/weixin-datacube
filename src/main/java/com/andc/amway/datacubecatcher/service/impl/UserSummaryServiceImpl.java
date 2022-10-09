package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserSummaryRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserSummary;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/20.
 */
@Service
public class UserSummaryServiceImpl extends AbstractCatchService {

    UserSummaryRepository userSummaryRepository;

    public UserSummaryServiceImpl(@Autowired UserSummaryRepository userSummaryRepository) {
        this.currentType = WxApiType.GetUserSummary;
        this.currentDataRepository = userSummaryRepository;
        this.userSummaryRepository = userSummaryRepository;
    }

    @Override
    protected List<UserSummary> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserSummary userSummary = JSONObject.parseObject(
                    json.toString(), UserSummary.class);
            userSummary.setAccount_id(accountId);
            return userSummary;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserSummary> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserSummary>)userSummaryRepository.save((List<UserSummary>)filterContent);
    }
}
