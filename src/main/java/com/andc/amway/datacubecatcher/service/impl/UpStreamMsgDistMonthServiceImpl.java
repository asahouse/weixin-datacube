package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgDistMonthRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgDistMonth;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgDistMonthServiceImpl extends AbstractCatchService{

    UpStreamMsgDistMonthRepository upStreamMsgDistMonthRepository;

    public UpStreamMsgDistMonthServiceImpl(@Autowired UpStreamMsgDistMonthRepository upStreamMsgDistMonthRepository){
        this.currentType = WxApiType.GetUpStreamMsgDistMonth;
        this.currentDataRepository = upStreamMsgDistMonthRepository;
        this.upStreamMsgDistMonthRepository = upStreamMsgDistMonthRepository;
    }

    @Override
    protected List<UpStreamMsgDistMonth> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgDistMonth upStreamMsgDistMonth = JSONObject.parseObject(
                    json.toString(), UpStreamMsgDistMonth.class);
            upStreamMsgDistMonth.setAccount_id(accountId);
            return upStreamMsgDistMonth;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgDistMonth> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgDistMonth>)upStreamMsgDistMonthRepository.save((List<UpStreamMsgDistMonth>)filterContent);
    }
}
