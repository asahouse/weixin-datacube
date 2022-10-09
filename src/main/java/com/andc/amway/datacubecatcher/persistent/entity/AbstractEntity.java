package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Data
@MappedSuperclass
public abstract class AbstractEntity<T> implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column
    protected String ref_date;

    @Column
    protected String account_id;
}
