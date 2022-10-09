package com.andc.amway.datacubecatcher.persistent.entity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.ParseProcess;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by benjaminkc on 17/7/3.
 *
 * 无接口对应, 主要用于分解ArticleTotal下的每日数据
 * 文章自发表后,7天内每天的统计数据
 *
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "msgid_index",columnList = "msgid"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "stat_date_index",columnList = "stat_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "ArticleTotalDetail_M_R_S_A_UNIQUE",
        columnNames = {"msgid", "ref_date", "stat_date", "account_id"}))
public class ArticleTotalDetail extends AbstractEntity<ArticleTotalDetail>{

    @Column
    private String msgid;
    @Column
    private String title;

    @Column
    private String stat_date;
    @Column
    private Long target_user;
    @Column
    private Long int_page_read_user;
    @Column
    private Long int_page_read_count;
    @Column
    private Long ori_page_read_user;
    @Column
    private Long ori_page_read_count;
    @Column
    private Long share_user;
    @Column
    private Long share_count;
    @Column
    private Long add_to_fav_user;
    @Column
    private Long add_to_fav_count;
    @Column
    private Long int_page_from_feed_read_user;
    @Column
    private Long int_page_from_feed_read_count;
    @Column
    private Long int_page_from_hist_msg_read_user;
    @Column
    private Long int_page_from_hist_msg_read_count;
    @Column
    private Long int_page_from_session_read_user;
    @Column
    private Long int_page_from_session_read_count;
    @Column
    private Long int_page_from_friends_read_user;
    @Column
    private Long int_page_from_friends_read_count;
    @Column
    private Long int_page_from_other_read_user;
    @Column
    private Long int_page_from_other_read_count;
    @Column
    private Long feed_share_from_session_user;
    @Column
    private Long feed_share_from_session_cnt;
    @Column
    private Long feed_share_from_feed_user;
    @Column
    private Long feed_share_from_feed_cnt;
    @Column
    private Long feed_share_from_other_user;
    @Column
    private Long feed_share_from_other_cnt;


    public static List<ArticleTotalDetail> trans(ArticleTotal parent){

        JSONArray array = JSONObject.parseArray(parent.getDetails());

        List<ArticleTotalDetail> result = new ArrayList<>();

        if (!array.isEmpty()){
            array.forEach(object ->{
                JSONObject jsonObject = (JSONObject) object;

                ArticleTotalDetail subject = JSONObject.parseObject(jsonObject.toJSONString(),
                        ArticleTotalDetail.class);
                subject.setTitle(parent.getTitle());
                subject.setAccount_id(parent.getAccount_id());
                subject.setMsgid(parent.getMsgid());
                subject.setRef_date(parent.getRef_date());

                result.add(subject);
            });
        }
        return result;
    }
}
