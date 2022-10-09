package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "count_interval_index",columnList = "count_interval"),
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        }, uniqueConstraints = @UniqueConstraint(name = "UpStreamMsgDist_C_U_R_A_UNIQUE",
        columnNames = {"count_interval", "user_source", "ref_date", "account_id"}))
public class UpStreamMsgDist extends AbstractEntity<UpStreamMsgDist> {
    @Column
    private Long count_interval;
    @Column
    private Long user_source;
    @Column
    private Long msg_user;
}
