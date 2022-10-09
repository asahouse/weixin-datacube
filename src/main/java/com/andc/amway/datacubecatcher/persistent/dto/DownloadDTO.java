package com.andc.amway.datacubecatcher.persistent.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DownloadDTO implements Serializable{
    private String ref_date;
    private String account_id;
}
