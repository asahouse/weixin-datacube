package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(indexes = {
        @Index(name = "ref_date_index",columnList = "ref_date"),
        @Index(name = "account_id_index",columnList = "account_id")
        }, uniqueConstraints = @UniqueConstraint(name = "InterfaceSummary_R_A_UNIQUE",
        columnNames = {"ref_date", "account_id"}))
public class InterfaceSummary extends AbstractEntity<InterfaceSummary> {
    @Column
    private Long callback_count;
    @Column
    private Long max_time_cost;
    @Column
    private Long total_time_cost;
    @Column
    private Long fail_count;
}
