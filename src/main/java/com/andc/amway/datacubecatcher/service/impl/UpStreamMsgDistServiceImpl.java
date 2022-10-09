package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.UpStreamMsgDistRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsgDist;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpStreamMsgDistServiceImpl extends AbstractCatchService{

    UpStreamMsgDistRepository upStreamMsgDistRepository;

    public UpStreamMsgDistServiceImpl(@Autowired UpStreamMsgDistRepository upStreamMsgDistRepository){
        this.currentType = WxApiType.GetUpStreamMsgDist;
        this.currentDataRepository = upStreamMsgDistRepository;
        this.upStreamMsgDistRepository = upStreamMsgDistRepository;
    }

    @Override
    protected List<UpStreamMsgDist> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            UpStreamMsgDist upStreamMsgDist = JSONObject.parseObject(
                    json.toString(), UpStreamMsgDist.class);
            upStreamMsgDist.setAccount_id(accountId);
            return upStreamMsgDist;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<UpStreamMsgDist> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<UpStreamMsgDist>)upStreamMsgDistRepository.save((List<UpStreamMsgDist>)filterContent);
    }
}
