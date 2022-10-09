package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@EqualsAndHashCode
@Entity
@Table(indexes = {
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "msg_type_index",columnList = "msg_type"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "ref_hour_index",columnList = "ref_hour"),
        @Index(name = "account_id_index",columnList = "account_id")
}, uniqueConstraints = @UniqueConstraint(name = "UpStreamMsgHour_U_M_R_R_A_UNIQUE",
        columnNames = {"user_source", "msg_type", "ref_date", "ref_hour", "account_id"}))
public class UpStreamMsgHour extends AbstractHourEntity<UpStreamMsgHour>  {
    @Column
    private Long user_source;
    @Column
    private Long msg_user;
    @Column
    private Long msg_type;
    @Column
    private Long msg_count;
}
