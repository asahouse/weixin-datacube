package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Created by benjaminkc on 17/7/3.
 */
@Data
@Entity
@Table(indexes = {
        @Index(name = "user_source_index",columnList = "user_source"),
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        },uniqueConstraints = @UniqueConstraint(name = "UserRead_U_R_A_UNIQUE",
        columnNames = {"user_source", "ref_date", "account_id"}))
public class UserRead extends AbstractEntity<UserRead>{

    @Column
    private Long share_count;
    @Column
    private Long share_user;
    @Column
    private Long add_to_fav_user;
    @Column
    private Long user_source;
    @Column
    private Long ori_page_read_user;
    @Column
    private Long int_page_read_user;
    @Column
    private Long add_to_fav_count;
    @Column
    private Long ori_page_read_count;
    @Column
    private Long int_page_read_count;
}
