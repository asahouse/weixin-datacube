package com.andc.amway.datacubecatcher.wx.source;

import java.util.List;

/**
 * Created by benjaminkc on 17/7/2.
 */
public interface SourceInterface {
    String achieveAccessToken(Account account);
    List<Account> achieveAccounts();
    void refreshAccounts();//刷新公众号集合
    void refreshSource();//刷新来源
}
