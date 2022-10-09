package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgHourRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgHour;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgHourServiceImpl extends AbstractCatchService{

    UpStreamMsgHourRepository upStreamMsgHourRepository;

    public UpStreamMsgHourServiceImpl(
            @Autowired UpStreamMsgHourRepository upStreamMsgHourRepository){

        this.currentType = WxApiType.GetUpStreamMsgHour;
        this.currentDataRepository = upStreamMsgHourRepository;
        this.upStreamMsgHourRepository = upStreamMsgHourRepository;
    }

    @Override
    protected List<UpStreamMsgHour> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgHour upStreamMsgHour = JSONObject.parseObject(
                    json.toString(), UpStreamMsgHour.class);
            upStreamMsgHour.setAccount_id(accountId);
            return upStreamMsgHour;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgHour> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgHour>)upStreamMsgHourRepository.save((List<UpStreamMsgHour>)filterContent);
    }
}
