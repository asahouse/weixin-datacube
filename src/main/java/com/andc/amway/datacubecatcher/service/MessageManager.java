package com.andc.amway.datacubecatcher.service;

import com.alibaba.fastjson.JSONObject;
import com.andc.amway.datacubecatcher.persistent.dao.ReceiveMessageEventRepository;
import com.andc.amway.datacubecatcher.persistent.dao.ReceiveMessageRepository;
import com.andc.amway.datacubecatcher.persistent.entity.ReceiveMessage;
import com.andc.amway.datacubecatcher.persistent.entity.ReceiveMessageEvent;
import com.andc.amway.datacubecatcher.wx.message.WxReceiveCommonMessage;
import com.andc.amway.datacubecatcher.wx.message.WxReceiveMessageEvent;
import com.andc.amway.datacubecatcher.wx.source.open.OpenTransferMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class MessageManager {

    private final ObjectMapper objectMapper;
    private final ReceiveMessageRepository receiveMessageRepository;
    private final ReceiveMessageEventRepository receiveMessageEventRepository;

    public MessageManager(MappingJackson2XmlHttpMessageConverter xmlConverter,
                          ReceiveMessageRepository receiveMessageRepository,
                          ReceiveMessageEventRepository receiveMessageEventRepository) {
        this.objectMapper = xmlConverter.getObjectMapper();
        this.receiveMessageRepository = receiveMessageRepository;
        this.receiveMessageEventRepository = receiveMessageEventRepository;
    }

    public void handleMessage(String message){
        OpenTransferMessage openTransferMessage = JSONObject.parseObject(message, OpenTransferMessage.class);

        CompletableFuture.runAsync(() -> {
            try{
                //消息记录
                WxReceiveCommonMessage commonMessage = objectMapper.readValue(
                        openTransferMessage.getContent(), WxReceiveCommonMessage.class);

                ReceiveMessage receiveMessage = ReceiveMessage.builder()
                        .accountId(openTransferMessage.getAppId())
                        .content(openTransferMessage.getContent())
                        .receiveTime(LocalDateTime.now())
                        .msgType(commonMessage.getMsgType().toString())
                        .build();
                BeanUtils.copyProperties(commonMessage, receiveMessage);
                receiveMessageRepository.save(receiveMessage);

                if (WxReceiveCommonMessage.MsgType.event.equals(commonMessage.getMsgType())) {
                    //消息中的事件记录
                    WxReceiveMessageEvent messageEvent = objectMapper.readValue(
                            openTransferMessage.getContent(), WxReceiveMessageEvent.class);

                    ReceiveMessageEvent receiveMessageEvent = ReceiveMessageEvent.builder()
                            .messageId(receiveMessage.getId())
                            .event(messageEvent.getEvent().toString())
                            .eventKey(messageEvent.getEventKey()).build();
                    receiveMessageEventRepository.save(receiveMessageEvent);
                }

            }catch (IOException ex){ logThrowable(ex, message); }

        }).handleAsync((result, ex) -> {
            Optional.ofNullable(ex).ifPresent(e -> logThrowable(e, message));
            return result;
        });
    }

    private void logThrowable(Throwable throwable, String content){
        log.error("=== Message Error Info ===");
        log.error("Receive content : " + content);
        log.error("Ex : " + throwable.toString());
        log.error("=== Message Error Info end===");
    }
}
