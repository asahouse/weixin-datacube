package com.andc.amway.datacubecatcher.wx.message.custommenu;

import com.andc.amway.datacubecatcher.wx.message.WxReceiveMessageEvent;
import com.andc.amway.datacubecatcher.wx.message.custommenu.info.WxReceiveMessageCustomSendPicsInfo;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 消息下的推送事件
 * pic_photo_or_album：弹出拍照或者相册发图的事件推送
 */
@Data
@XmlRootElement
public class WxReceiveMessageCustomPicPhotoOrAlbumEvent extends WxReceiveMessageEvent {
    @JacksonXmlProperty(localName = "SendPicsInfo")
    private WxReceiveMessageCustomSendPicsInfo sendPicsInfo;
}
