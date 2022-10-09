package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.InterfaceSummaryHourRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.InterfaceSummaryHour;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterfaceSummaryHourServiceImpl extends AbstractCatchService {

    InterfaceSummaryHourRepository interfaceSummaryHourRepository;

    public InterfaceSummaryHourServiceImpl(@Autowired InterfaceSummaryHourRepository interfaceSummaryHourRepository) {
        this.currentType = WxApiType.GetInterfaceSummaryHour;
        this.currentDataRepository = interfaceSummaryHourRepository;
        this.interfaceSummaryHourRepository = interfaceSummaryHourRepository;
    }

    @Override
    protected List<InterfaceSummaryHour> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            InterfaceSummaryHour interfaceSummaryHour = JSONObject.parseObject(
                    json.toString(), InterfaceSummaryHour.class);
            interfaceSummaryHour.setAccount_id(accountId);
            return interfaceSummaryHour;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<InterfaceSummaryHour> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<InterfaceSummaryHour>)interfaceSummaryHourRepository.save((List<InterfaceSummaryHour>)filterContent);
    }
}
