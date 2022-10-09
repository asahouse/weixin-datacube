package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.service.MessageManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/receive/message")
public class ReceiveController {

    private final MessageManager messageManager;

    public ReceiveController(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @PostMapping
    public void receive(@RequestBody String content){
        log.debug("ReceiveController -> " + content);
        messageManager.handleMessage(content);
    }
}
