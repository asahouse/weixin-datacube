package com.andc.amway.datacubecatcher.persistent.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by benjaminkc on 17/7/3.
 * 文章自发表后,7天内的所有统计数据
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "msgid_index",columnList = "msgid"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        },
        uniqueConstraints = @UniqueConstraint(name = "ArticleTotal_M_R_A_UNIQUE",
        columnNames = {"msgid", "ref_date", "account_id"}))
public class ArticleTotal extends AbstractEntity<ArticleTotal>{

    @Column
    private String title;

    @Column
    private String msgid;

    @Column(columnDefinition="Blob")
    private String details;//Blob->JSON

    public static ArticleTotal trans(JSONObject json){

        String title = json.getString("title");
        String msgid = json.getString("msgid");
        String ref_date = json.getString("ref_date");

        String details = json.getString("details");

        ArticleTotal subject = new ArticleTotal();
            subject.setTitle(title);
            subject.setMsgid(msgid);
            subject.setRef_date(ref_date);
            subject.setDetails(details);
        return subject;
    }
}
