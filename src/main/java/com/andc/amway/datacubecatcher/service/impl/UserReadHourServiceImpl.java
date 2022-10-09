package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserReadHourRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserReadHour;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Service
public class UserReadHourServiceImpl extends AbstractCatchService {

    UserReadHourRepository userReadHourRepository;

    public UserReadHourServiceImpl(@Autowired UserReadHourRepository userReadHourRepository) {
        this.currentType = WxApiType.GetUserReadHour;
        this.currentDataRepository = userReadHourRepository;
        this.userReadHourRepository = userReadHourRepository;
    }

    @Override
    protected List<UserReadHour> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserReadHour userReadHour = JSONObject.parseObject(
                    json.toString(), UserReadHour.class);
                userReadHour.setAccount_id(accountId);
            return userReadHour;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserReadHour> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserReadHour>)userReadHourRepository.save((List<UserReadHour>)filterContent);
    }
}
