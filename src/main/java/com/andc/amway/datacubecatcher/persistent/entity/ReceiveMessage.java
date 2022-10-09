package com.andc.amway.datacubecatcher.persistent.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by benjaminkc on 18/1/24.
 * 接收消息
 */
@Data
@Builder
@Entity
public class ReceiveMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String accountId;

    @Column(columnDefinition="Blob")
    private String content;

    @Column
    private LocalDateTime receiveTime;

    @Column
    private String toUserName;

    @Column
    private String fromUserName;

    @Column
    private String msgType;

    @Column
    private Long createTime;
}
