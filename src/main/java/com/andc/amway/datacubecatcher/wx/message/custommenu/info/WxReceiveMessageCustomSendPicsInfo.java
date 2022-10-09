package com.andc.amway.datacubecatcher.wx.message.custommenu.info;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 消息下的推送事件
 * pic_sysphoto：弹出系统拍照发图的事件推送
 */
@Data
public class WxReceiveMessageCustomSendPicsInfo implements Serializable {

    @JacksonXmlProperty(localName = "Count")
    private String count;

    @JacksonXmlElementWrapper(useWrapping=false, localName = "PicList")
    private List<WxReceiveMessageCustomSendPicsInfo> picList;

    @Data
    class WxReceiveMessageCustomPicItem implements Serializable {
        @JacksonXmlElementWrapper(useWrapping=false, localName = "item")
        private List<WxReceiveMessageCustomPicItemMD5Sum> items;
    }

    @Data
    class WxReceiveMessageCustomPicItemMD5Sum implements Serializable {
        @JacksonXmlProperty(localName = "PicMd5Sum")
        private String picMd5Sum;
    }
}
