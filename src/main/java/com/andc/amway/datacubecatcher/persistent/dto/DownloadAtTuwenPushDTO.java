package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.Data;

@Data
public class DownloadAtTuwenPushDTO extends DownloadDTO{
    private String stat_date;
    private String title;
    private long int_page_read_user;//打开人数
    private Double int_page_read_user_rate;//打开率
    private long target_user;//送达人数
    private Double target_user_rate;//送达率
    private long share_user;//分享人数
    private Double share_rate;//分享率
}
