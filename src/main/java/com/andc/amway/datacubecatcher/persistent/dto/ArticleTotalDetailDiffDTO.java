package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTotalDetailDiffDTO implements Serializable{

    private String account_id;
    private String ref_date;

    public ArticleTotalDetailDiffDTO(Object[] o){
        this(o[0].toString(), o[1].toString());
    }
}
