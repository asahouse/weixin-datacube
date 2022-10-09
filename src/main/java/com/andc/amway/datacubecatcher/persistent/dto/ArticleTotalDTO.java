package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Date;

/**
 * Created by benjaminkc on 17/7/12.
 */
@AllArgsConstructor
@Data
public class ArticleTotalDTO implements Serializable {

    private Date refDate;
    private String title;
    private BigInteger targetUser;
    private BigInteger intPageReadUser;
    private BigInteger shareUser;
    private String accountId;
}
