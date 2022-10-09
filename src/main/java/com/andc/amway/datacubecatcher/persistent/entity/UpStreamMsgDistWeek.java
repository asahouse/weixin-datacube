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
        }, uniqueConstraints = @UniqueConstraint(name = "UpStreamMsgDistWeek_C_U_R_A_UNIQUE",
        columnNames = {"count_interval", "user_source", "ref_date", "account_id"}))
public class UpStreamMsgDistWeek extends AbstractEntity<UpStreamMsgDistWeek> {
    @Column
    private Long count_interval;
    @Column
    private Long user_source;
    @Column
    private Long msg_user;
}
