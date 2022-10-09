package com.andc.amway.datacubecatcher.wx.source;

import com.andc.amway.datacubecatcher.wx.source.digi.ProxyDigiApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by benjaminkc on 17/7/2.
 */
@Slf4j
@Component
public class DigiSource implements SourceInterface {

    public static String DIGI_PLATFORM_FLAG = "Digi";
    private ProxyDigiApi proxyDigiApi;
    private List<Account> accounts = new ArrayList<>();
    private String digi_accounts;

    public DigiSource(@Autowired ProxyDigiApi proxyDigiApi,
                      @Value("${source.platform}") String platform,
                      @Value("${source.digi.accounts}") String digi_accounts) {

        this.proxyDigiApi = proxyDigiApi;
        this.digi_accounts = digi_accounts;

        if (DIGI_PLATFORM_FLAG.equals(platform)) {
            log.info("init DigiSource start");
            this.refreshAccounts();
            log.info("init DigiSource end");
        }
    }

    @Override
    public String achieveAccessToken(Account account) {
        return proxyDigiApi.callAccessToken(account).get("access_token").toString();
    }

    @Override
    public List<Account> achieveAccounts() {
        return accounts;
    }

    @Override
    public void refreshAccounts(){
        if (StringUtils.isEmpty(digi_accounts)) {
            throw new RuntimeException("Can not found Digi ACCOUNTS!");
        }
        Arrays.asList(digi_accounts.split(","))
                .stream().forEach(str -> {
            String[] account = str.split(":");
            Account object = new Account();
            object.setId(account[0]);
            object.setName(account[1]);
            object.setSite(account[2]);
            object.setSn(account[3]);
            accounts.add(object);
        });
    }

    public void refreshSource(){}
}
