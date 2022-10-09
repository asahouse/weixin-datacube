package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserShareRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserShare;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Service
public class UserShareServiceImpl extends AbstractCatchService {

    UserShareRepository userShareRepository;

    public UserShareServiceImpl(@Autowired UserShareRepository userShareRepository) {
        this.currentType = WxApiType.GetUserShare;
        this.currentDataRepository = userShareRepository;
        this.userShareRepository = userShareRepository;
    }

    @Override
    protected List<UserShare> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserShare userShare = JSONObject.parseObject(
                    json.toString(), UserShare.class);
                userShare.setAccount_id(accountId);
            return userShare;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserShare> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserShare>)userShareRepository.save((List<UserShare>)filterContent);
    }
}
