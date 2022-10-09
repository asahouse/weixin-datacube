package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.ArticleSummaryRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.ArticleSummary;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Service
public class ArticleSummaryServiceImpl extends AbstractCatchService {

    ArticleSummaryRepository articleSummaryRepository;

    public ArticleSummaryServiceImpl(@Autowired ArticleSummaryRepository articleSummaryRepository) {
        this.currentType = WxApiType.GetArticleSummary;
        this.currentDataRepository = articleSummaryRepository;
        this.articleSummaryRepository = articleSummaryRepository;
    }

    @Override
    protected List<ArticleSummary> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            ArticleSummary articleSummary = JSONObject.parseObject(
                    json.toString(), ArticleSummary.class);
                articleSummary.setAccount_id(accountId);
            return articleSummary;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<ArticleSummary> save(String accountId, List<? extends AbstractEntity> filterContent){
        return (List<ArticleSummary>)articleSummaryRepository.save((List<ArticleSummary>)filterContent);
    }
}
