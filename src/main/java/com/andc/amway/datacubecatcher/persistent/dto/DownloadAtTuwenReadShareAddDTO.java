package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.Data;

@Data
public class DownloadAtTuwenReadShareAddDTO extends DownloadDTO {
    private String title;
    private String stat_date;
    private long share_user;
    private long share_count;
    private Double share_rate;
    private long int_page_read_user;
    private long int_page_read_count;
    private long ori_page_read_user;
    private long ori_page_read_count;
    private long int_page_from_feed_read_user;
    private long int_page_from_feed_read_count;
    private long int_page_from_hist_msg_read_user;
    private long int_page_from_hist_msg_read_count;
    private long int_page_from_session_read_user;
    private long int_page_from_session_read_count;
    private long int_page_from_friends_read_user;
    private long int_page_from_friends_read_count;
    private long int_page_from_other_read_user;
    private long int_page_from_other_read_count;
    private long feed_share_from_session_user;
    private long feed_share_from_session_cnt;
    private long feed_share_from_feed_user;
    private long feed_share_from_feed_cnt;
    private long feed_share_from_other_user;
    private long feed_share_from_other_cnt;
    private long add_to_fav_user;
    private long add_to_fav_count;
}
