package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by benjaminkc on 18/1/24.
 * 接收消息中的事件
 */
@Data
@Builder
@Entity
public class ReceiveMessageEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long messageId;

    @Column
    private String event;

    @Column
    private String eventKey;
}
