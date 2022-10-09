package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.InterfaceSummaryRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.InterfaceSummary;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterfaceSummaryServiceImpl extends AbstractCatchService {

    InterfaceSummaryRepository interfaceSummaryRepository;

    public InterfaceSummaryServiceImpl(@Autowired InterfaceSummaryRepository interfaceSummaryRepository){
        this.currentType = WxApiType.GetInterfaceSummary;
        this.currentDataRepository = interfaceSummaryRepository;
        this.interfaceSummaryRepository = interfaceSummaryRepository;
    }

    @Override
    protected List<InterfaceSummary> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            InterfaceSummary interfaceSummary = JSONObject.parseObject(
                    json.toString(), InterfaceSummary.class);
            interfaceSummary.setAccount_id(accountId);
            return interfaceSummary;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<InterfaceSummary> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<InterfaceSummary>)interfaceSummaryRepository.save((List<InterfaceSummary>)filterContent);
    }
}
