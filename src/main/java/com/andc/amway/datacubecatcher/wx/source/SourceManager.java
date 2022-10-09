package com.andc.amway.datacubecatcher.wx.source;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/7/2.
 */
@Service
public class SourceManager {
    SourceInterface source;
    public SourceType currentType;

    public SourceManager(@Autowired SourceFactory sourceFactory,
                         @Value("${source.platform}") String accountPlatform){
        currentType = SourceType.fromName(accountPlatform);
        source = sourceFactory.createAccountSource(currentType);
    }

    public List<Account> findAllAccounts(){
        return source.achieveAccounts();
    }

    public List<Account> findAccountByIds(List<String> ids){
        return source.achieveAccounts().stream()
                .filter(account -> ids.contains(account.getId()))
                .collect(Collectors.toList());
    }

    public String callAccessToken(Account account){
        return source.achieveAccessToken(account);
    }

    public void refresh(){
        source.refreshSource();
    }
}
