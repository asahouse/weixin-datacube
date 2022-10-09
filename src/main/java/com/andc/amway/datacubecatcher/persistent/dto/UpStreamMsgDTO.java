package com.andc.amway.datacubecatcher.persistent.dto;

import com.andc.amway.datacubecatcher.persistent.entity.UpStreamMsg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpStreamMsgDTO {

    private String ref_date;
    private Integer account_id;
    private Long msg_user_total;
    private Long msg_count_total;

    private List<UpStreamMsg> records;
}
