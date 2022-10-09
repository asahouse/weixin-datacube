package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by benjaminkc on 17/7/20.
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        }, uniqueConstraints = @UniqueConstraint(name = "UserSummary_U_R_A_UNIQUE",
        columnNames = {"user_source", "ref_date", "account_id"}))
public class UserSummary extends AbstractEntity<UserSummary>{

    @Column
    private Integer user_source;
    @Column
    private Long new_user;
    @Column
    private Long cancel_user;
}
