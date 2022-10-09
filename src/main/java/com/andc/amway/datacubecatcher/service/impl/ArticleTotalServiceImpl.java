package com.andc.amway.datacubecatcher.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.dao.ArticleTotalDetailRepository;
import com.andc.amway.datacubecatcher.persistent.dao.ArticleTotalRepository;
import com.andc.amway.datacubecatcher.persistent.entity.AbstractEntity;
import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotal;
import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotalDetail;
import com.andc.amway.datacubecatcher.service.inter.AbstractCatchService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/4.
 */
@Service
public class ArticleTotalServiceImpl extends AbstractCatchService {

    ArticleTotalRepository totalRepository;
    ArticleTotalDetailRepository totalDetailRepository;

    public ArticleTotalServiceImpl(@Autowired ArticleTotalRepository totalRepository,
                                   @Autowired ArticleTotalDetailRepository totalDetailRepository) {
        this.currentType = WxApiType.GetArticleTotal;
        this.currentDataRepository = totalRepository;
        this.totalRepository = totalRepository;
        this.totalDetailRepository = totalDetailRepository;
    }

    /**
     * 特殊逻辑:
     * 由于[图文群发总数据] 接口获取的文章数量不变,
     * 但每篇文章的返回监察日期的统计信息在发表后7日内每天会叠加,直到第七日停止
     * => 所以作为接口记录的total在记录后, 还可以按查询更新deltails -> BLOB内容
     * => 但total下的每个detail还是需要保存, 同时也先查询过滤已存在的detail
     */
    @Transactional
    protected List<ArticleTotal> process(String accountId, JSONArray catchContent) {
        return catchContent.parallelStream().map(json -> {
            ArticleTotal articleTotal = ArticleTotal.trans((JSONObject) json);
                articleTotal.setAccount_id(accountId);
                articleTotal.setId(null);
            return articleTotal;
        }).collect(Collectors.toList());
    }

    @Override
    protected List<ArticleTotal> save(String accountId, List<? extends AbstractEntity> filterContent){
        List<ArticleTotal> news = (List<ArticleTotal>) filterContent;

        //已存在的文章(msgid与文章是唯一关系)
        List<ArticleTotal> exists = totalRepository.findByMsgidIn(
                news.parallelStream()
                        .map(ArticleTotal::getMsgid)
                        .collect(Collectors.toList()))
                .parallelStream()
                .collect(Collectors.toList());

        //已存在集合按msgid分组
        Map<String, List<ArticleTotal>> existsGroup =
                exists.parallelStream().collect(Collectors.groupingBy(ArticleTotal::getMsgid));

        List<ArticleTotal> saveArticleTotals =
                news.parallelStream().map(newOne -> {
                    ArticleTotal single = new ArticleTotal();
                    //获取已存在对象,更新details

                    if (!existsGroup.isEmpty()){
                        Optional<ArticleTotal> existOptional = existsGroup.get(newOne.getMsgid()).stream().findFirst();
                        if (existOptional.isPresent()) {
                            ArticleTotal exist = existOptional.get();
                            exist.setDetails(newOne.getDetails());
                            BeanUtils.copyProperties(exist, single);
                        }
                    }else BeanUtils.copyProperties(newOne, single);

                    return single;
                }).collect(Collectors.toList());

        /**
         * 处理details
         */
        //从JSON原数据中提取detail集合
        List<ArticleTotalDetail> articleTotalDetails =
                news.parallelStream().flatMap(articleTotal ->
                        ArticleTotalDetail.trans(articleTotal).parallelStream())
                        .collect(Collectors.toList());

        //按msgid分组
        Map<String, List<ArticleTotalDetail>> msgidGroups = articleTotalDetails.parallelStream()
                .collect(Collectors.groupingBy(ArticleTotalDetail::getMsgid));

        //过滤已存在的msgid / starDate的detail
        List<ArticleTotalDetail> saveArticleTotalDetails =
                msgidGroups.entrySet().parallelStream().flatMap(e -> {
                    e.getValue().removeAll(
                            totalDetailRepository.findByMsgidAndStatDates(
                                    e.getKey(),//msgid
                                    e.getValue().parallelStream()
                                            .map(ArticleTotalDetail::getStat_date)
                                            .collect(Collectors.toList())//stat_dats集合
                            )
                    );//差集
                    return e.getValue().parallelStream();
                }).collect(Collectors.toList());


        /**
         * 保存
         */
        totalRepository.save(saveArticleTotals);
        totalDetailRepository.save(saveArticleTotalDetails);

        return saveArticleTotals;
    }
}
