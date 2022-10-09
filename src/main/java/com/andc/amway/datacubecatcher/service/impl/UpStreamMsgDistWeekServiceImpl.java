package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgDistWeekRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgDistWeek;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgDistWeekServiceImpl extends AbstractCatchService{

    UpStreamMsgDistWeekRepository upStreamMsgDistWeekRepository;

    public UpStreamMsgDistWeekServiceImpl(@Autowired UpStreamMsgDistWeekRepository upStreamMsgDistWeekRepository){
        this.currentType = WxApiType.GetUpStreamMsgDistWeek;
        this.currentDataRepository = upStreamMsgDistWeekRepository;
        this.upStreamMsgDistWeekRepository = upStreamMsgDistWeekRepository;
    }

    @Override
    protected List<UpStreamMsgDistWeek> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgDistWeek upStreamMsgDistWeek = JSONObject.parseObject(
                    json.toString(), UpStreamMsgDistWeek.class);
            upStreamMsgDistWeek.setAccount_id(accountId);
            return upStreamMsgDistWeek;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgDistWeek> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgDistWeek>)upStreamMsgDistWeekRepository.save((List<UpStreamMsgDistWeek>)filterContent);
    }
}
