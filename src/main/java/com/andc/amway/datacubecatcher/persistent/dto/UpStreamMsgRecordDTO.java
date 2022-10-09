package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpStreamMsgRecordDTO {

    private Integer account_id;
    private String ref_date;
    private Long msg_user_total;
    private Long msg_count_total;

    public UpStreamMsgRecordDTO(Object[] o){
        this(Integer.valueOf(o[0].toString()), o[1].toString(),
                Long.valueOf(o[2].toString()),
                Long.valueOf(o[3].toString()));
    }
}
