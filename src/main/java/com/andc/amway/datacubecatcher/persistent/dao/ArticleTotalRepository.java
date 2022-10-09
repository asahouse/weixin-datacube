package com.andc.amway.datacubecatcher.persistent.dao;

import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotal;

import java.util.List;

/**
 * Created by benjaminkc on 17/7/3.
 */
public interface ArticleTotalRepository extends BasicDataRepository<ArticleTotal> {
    List<ArticleTotal> findByMsgidIn(List<String> msgids);
}
