package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by benjaminkc on 17/7/10.
 */
@Data
@MappedSuperclass
public abstract class AbstractHourEntity<T> extends AbstractEntity{

    @Column
    protected String ref_hour;
}
