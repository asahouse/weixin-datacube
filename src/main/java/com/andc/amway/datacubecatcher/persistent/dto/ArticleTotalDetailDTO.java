package com.andc.amway.datacubecatcher.persistent.dto;

import com.andc.amway.datacubecatcher.persistent.entity.ArticleTotalDetail;
import lombok.Data;

@Data
public class ArticleTotalDetailDTO extends ArticleTotalDetail {
    private Double share_rate;
}
