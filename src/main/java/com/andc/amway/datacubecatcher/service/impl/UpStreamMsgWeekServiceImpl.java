package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgWeekRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgWeek;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgWeekServiceImpl extends AbstractCatchService {

    UpStreamMsgWeekRepository upStreamMsgWeekRepository;

    public UpStreamMsgWeekServiceImpl(@Autowired UpStreamMsgWeekRepository upStreamMsgWeekRepository){
        this.currentType = WxApiType.GetUpStreamMsgWeek;
        this.currentDataRepository = upStreamMsgWeekRepository;
        this.upStreamMsgWeekRepository = upStreamMsgWeekRepository;
    }

    @Override
    protected List<UpStreamMsgWeek> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgWeek upStreamMsgWeek = JSONObject.parseObject(
                    json.toString(), UpStreamMsgWeek.class);
            upStreamMsgWeek.setAccount_id(accountId);
            return upStreamMsgWeek;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgWeek> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgWeek>) upStreamMsgWeekRepository.save((List<UpStreamMsgWeek>)filterContent);
    }
}
