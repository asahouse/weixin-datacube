package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsg;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgServiceImpl extends AbstractCatchService{

    UpStreamMsgRepository upStreamMsgRepository;

    public UpStreamMsgServiceImpl(@Autowired UpStreamMsgRepository upStreamMsgRepository){
        this.currentType = WxApiType.GetUpStreamMsg;
        this.currentDataRepository = upStreamMsgRepository;
        this.upStreamMsgRepository = upStreamMsgRepository;
    }

    @Override
    protected List<UpStreamMsg> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsg upStreamMsg = JSONObject.parseObject(
                    json.toString(), UpStreamMsg.class);
            upStreamMsg.setAccount_id(accountId);
            return upStreamMsg;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsg> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsg>)upStreamMsgRepository.save((List<UpStreamMsg>)filterContent);
    }
}
