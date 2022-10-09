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
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        },uniqueConstraints = @UniqueConstraint(name = "UserCumulate_R_A_UNIQUE",
        columnNames = {"ref_date", "account_id"}))
public class UserCumulate extends AbstractEntity<UserCumulate>{

    @Column
    private Long cumulate_user;
}
