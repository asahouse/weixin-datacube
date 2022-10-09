package com.andc.amway.datacubecatcher.wx.source;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by benjaminkc on 17/7/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable{
    //公用
    private String id;//open下为appId
    private String name;//open下为nickName

    //digi
    @JsonIgnore
    private String site;
    @JsonIgnore
    private String sn;

    //Open
    private String headImg;
    private String serviceTypeId;
    private String verifyTypeId;
    private String originalId;
    private String principalName;
    private String qrCodeUrl;

}
