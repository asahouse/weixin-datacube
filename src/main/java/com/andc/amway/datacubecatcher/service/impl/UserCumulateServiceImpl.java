package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserCumulateRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserCumulate;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/20.
 */
@Service
public class UserCumulateServiceImpl extends AbstractCatchService {

    UserCumulateRepository userCumulateRepository;

    public UserCumulateServiceImpl(@Autowired UserCumulateRepository userCumulateRepository) {
        this.currentType = WxApiType.GetUserCumulate;
        this.currentDataRepository = userCumulateRepository;
        this.userCumulateRepository = userCumulateRepository;
    }

    @Override
    protected List<UserCumulate> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserCumulate userCumulate = JSONObject.parseObject(
                    json.toString(), UserCumulate.class);
            userCumulate.setAccount_id(accountId);
            return userCumulate;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserCumulate> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserCumulate>)userCumulateRepository.save((List<UserCumulate>)filterContent);
    }
}
