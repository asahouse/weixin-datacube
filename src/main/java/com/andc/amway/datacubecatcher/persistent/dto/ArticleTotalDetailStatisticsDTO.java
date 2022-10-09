package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleTotalDetailStatisticsDTO {
    private BigDecimal int_page_read_user;
    private BigDecimal int_page_read_count;
    private BigDecimal ori_page_read_user;
    private BigDecimal ori_page_read_count;
    private BigDecimal int_page_from_feed_read_user;
    private BigDecimal int_page_from_feed_read_count;
    private BigDecimal int_page_from_hist_msg_read_user;
    private BigDecimal int_page_from_hist_msg_read_count;
    private BigDecimal int_page_from_session_read_user;
    private BigDecimal int_page_from_session_read_count;
    private BigDecimal int_page_from_friends_read_user;
    private BigDecimal int_page_from_friends_read_count;
    private BigDecimal int_page_from_other_read_user;
    private BigDecimal int_page_from_other_read_count;
    private BigDecimal feed_share_from_session_user;
    private BigDecimal feed_share_from_session_cnt;
    private BigDecimal feed_share_from_feed_user;
    private BigDecimal feed_share_from_feed_cnt;
    private BigDecimal feed_share_from_other_user;
    private BigDecimal feed_share_from_other_cnt;
    private BigDecimal add_to_fav_user;
    private BigDecimal add_to_fav_count;
    private String stat_date;

    public ArticleTotalDetailStatisticsDTO(Object[] o){
        this(new BigDecimal(o[0].toString()), new BigDecimal(o[1].toString()),
                new BigDecimal(o[2].toString()), new BigDecimal(o[3].toString()),
                new BigDecimal(o[4].toString()), new BigDecimal(o[5].toString()),
                new BigDecimal(o[6].toString()), new BigDecimal(o[7].toString()),
                new BigDecimal(o[8].toString()), new BigDecimal(o[9].toString()),
                new BigDecimal(o[10].toString()), new BigDecimal(o[11].toString()),
                new BigDecimal(o[12].toString()), new BigDecimal(o[13].toString()),
                new BigDecimal(o[14].toString()), new BigDecimal(o[15].toString()),
                new BigDecimal(o[16].toString()), new BigDecimal(o[17].toString()),
                new BigDecimal(o[18].toString()), new BigDecimal(o[19].toString()),
                new BigDecimal(o[20].toString()), new BigDecimal(o[21].toString()),
                o[22].toString());
    }
}
