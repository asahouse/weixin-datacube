package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by benjaminkc on 17/7/3.
 *
 * 每篇文章指定日期下的概括统计数据
 *
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "msgid_index",columnList = "msgid"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "ArticleSummary_M_R_A_UNIQUE",
        columnNames = {"msgid", "ref_date", "account_id"}))
public class ArticleSummary extends AbstractEntity<ArticleSummary>{

    @Column
    private String msgid;
    @Column
    private String title;


    @Column
    private Long share_count;
    @Column
    private Long share_user;
    @Column
    private Long add_to_fav_user;
    @Column
    private Long add_to_fav_count;
    @Column
    private Long int_page_read_user;
    @Column
    private Long int_page_read_count;
    @Column
    private Long ori_page_read_user;
    @Column
    private Long ori_page_read_count;
}
