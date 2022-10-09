package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UserShareHourRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UserShareHour;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Service
public class UserShareHourServiceImpl extends AbstractCatchService {

    UserShareHourRepository userShareHourRepository;

    public UserShareHourServiceImpl(@Autowired UserShareHourRepository userShareHourRepository) {
        this.currentType = WxApiType.GetUserShareHour;
        this.currentDataRepository = userShareHourRepository;
        this.userShareHourRepository = userShareHourRepository;
    }

    @Override
    protected List<UserShareHour> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UserShareHour userShareHour = JSONObject.parseObject(
                    json.toString(), UserShareHour.class);
                userShareHour.setAccount_id(accountId);
            return userShareHour;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UserShareHour> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UserShareHour>)userShareHourRepository.save((List<UserShareHour>)filterContent);
    }
}
