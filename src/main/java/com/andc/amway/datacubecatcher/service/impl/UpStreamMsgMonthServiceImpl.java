package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgMonthRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgMonth;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgMonthServiceImpl extends AbstractCatchService{

    UpStreamMsgMonthRepository upStreamMsgMonthRepository;

    public UpStreamMsgMonthServiceImpl(@Autowired UpStreamMsgMonthRepository upStreamMsgMonthRepository){
        this.currentType = WxApiType.GetUpStreamMsgMonth;
        this.currentDataRepository = upStreamMsgMonthRepository;
        this.upStreamMsgMonthRepository = upStreamMsgMonthRepository;
    }

    @Override
    protected List<UpStreamMsgMonth> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgMonth upStreamMsgMonth = JSONObject.parseObject(
                    json.toString(), UpStreamMsgMonth.class);
            upStreamMsgMonth.setAccount_id(accountId);
            return upStreamMsgMonth;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgMonth> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgMonth>)upStreamMsgMonthRepository.save((List<UpStreamMsgMonth>)filterContent);
    }
}
