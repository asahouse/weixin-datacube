package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "msg_type_index",columnList = "msg_type"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        }, uniqueConstraints = @UniqueConstraint(name = "UpStreamMsg_U_M_R_A_UNIQUE",
        columnNames = {"user_source", "msg_type", "ref_date", "account_id"}))
public class UpStreamMsg extends AbstractEntity<UpStreamMsg> {
    @Column
    private Integer user_source;
    @Column
    private Long msg_user;
    @Column
    private Integer msg_type;
    @Column
    private Long msg_count;
}
