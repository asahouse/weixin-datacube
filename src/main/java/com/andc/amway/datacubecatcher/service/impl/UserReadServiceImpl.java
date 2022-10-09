package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserReadRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserRead;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Service
public class UserReadServiceImpl extends AbstractCatchService {

    UserReadRepository userReadRepository;

    public UserReadServiceImpl(@Autowired UserReadRepository userReadRepository) {
        this.currentType = WxApiType.GetUserRead;
        this.currentDataRepository = userReadRepository;
        this.userReadRepository = userReadRepository;
    }

    @Override
    protected List<UserRead> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserRead userRead = JSONObject.parseObject(
                    json.toString(), UserRead.class);
                userRead.setAccount_id(accountId);
            return userRead;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserRead> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserRead>)userReadRepository.save((List<UserRead>)filterContent);
    }
}
