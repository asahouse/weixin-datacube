package com.andc.amway.datacubecatcher.wx.source;

import com.alibaba.fastjson.JSON;
import com.andc.amway.datacubecatcher.wx.source.open.ProxyOpenApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by benjaminkc on 17/8/25.
 */
@Slf4j
@Component
public class OpenSource implements SourceInterface {

    public static String OPEN_PLATFORM_FLAG = "Open";
    private ProxyOpenApi proxyOpenApi;
    private List<Account> accounts = new ArrayList<>();

    public OpenSource(@Autowired ProxyOpenApi proxyOpenApi,
                      @Value("${source.platform}") String platform) {
        this.proxyOpenApi = proxyOpenApi;

        if (OPEN_PLATFORM_FLAG.equals(platform)) {
            log.info("init OpenSource start");
            this.refreshAccounts();
            log.info("init OpenSource end");
        }
    }

    @Override
    public String achieveAccessToken(Account account) {
        return proxyOpenApi.callAccessToken(account.getId()).get("accessToken").toString();
    }

    @Override
    public List<Account> achieveAccounts() {
        return accounts;
    }

    @Override
    public void refreshAccounts(){
        Map<String, Object> info = proxyOpenApi.getOpenAuthInfo();
        accounts = Arrays.stream(info.get("scope").toString().split(" ")).map(appId -> {
            Account account = new Account();
            Map<String, Object> details = proxyOpenApi.callDetail(appId);
            if (!details.isEmpty()) {
                BeanUtils.copyProperties(JSON.parseObject(JSON.toJSONString(details), Account.class), account);

                account.setId(details.get("appId").toString());
                account.setName(details.get("nickName").toString());
            }
            return account;
        }).filter(account -> Optional.ofNullable(account.getId()).isPresent()).collect(Collectors.toList());
    }

    public void refreshSource(){
        //1) 刷新第三方平台授权数据
        proxyOpenApi.getOpenAuthInfo();
    }
}
