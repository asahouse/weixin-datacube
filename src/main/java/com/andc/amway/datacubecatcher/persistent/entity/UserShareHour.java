package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by benjaminkc on 17/7/3.
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "ref_hour_index",columnList = "ref_hour"),
        @Index(name = "share_scene_index",columnList = "share_scene"),
        @Index(name = "account_id_index",columnList = "account_id")
        },uniqueConstraints = @UniqueConstraint(name = "UserShareHour_U_R_R_S_A_UNIQUE",
        columnNames = {"user_source", "ref_date", "ref_hour", "share_scene", "account_id"}))
public class UserShareHour extends AbstractHourEntity<UserShareHour>{
    @Column
    private Long share_count;
    @Column
    private Long share_user;
    @Column
    private Long user_source;
    @Column
    private Long share_scene;
}
