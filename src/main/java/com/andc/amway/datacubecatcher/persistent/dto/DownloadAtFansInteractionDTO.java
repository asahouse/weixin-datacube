package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.Data;

@Data
public class DownloadAtFansInteractionDTO extends DownloadDTO {

    private long msg_user_total;
    private long msg_count_total;

    private long msg_user_wz;
    private long msg_count_wz;

    private long msg_user_tp;
    private long msg_count_tp;

    private long msg_user_yy;
    private long msg_count_yy;

    private long msg_user_sp;
    private long msg_count_sp;

    private long msg_user_dsfyy;
    private long msg_count_dsfyy;

}
