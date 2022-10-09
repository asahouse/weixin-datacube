package com.andc.amway.datacubecatcher.wx.source.open;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenTransferMessage implements Serializable {
    private String appId;
    private String url;
    private String content;
}
