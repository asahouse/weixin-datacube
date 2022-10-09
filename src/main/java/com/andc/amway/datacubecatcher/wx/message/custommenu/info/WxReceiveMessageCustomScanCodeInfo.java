package com.andc.amway.datacubecatcher.wx.message.custommenu.info;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息下的推送事件
 * pic_sysphoto：弹出系统拍照发图的事件推送
 */
@Data
public class WxReceiveMessageCustomScanCodeInfo implements Serializable {

    @JacksonXmlProperty(localName = "ScanType")
    private String scanType;

    @JacksonXmlProperty(localName = "ScanResult")
    private String scanResult;
}
