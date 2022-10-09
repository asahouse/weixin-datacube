package com.andc.amway.datacubecatcher.async;

import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.api.WxApiType;
import com.andc.amway.datacubecatcher.persistent.entity.CatchEventLog;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by benjaminkc on 17/8/8.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FutureResult implements Serializable {

    private WxApiType wxApiType;
    private Account account;
    private DatePair datePair;
    private CatchEventLog catchEventLog;

}
