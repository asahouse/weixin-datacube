package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.wx.source.Account;
import com.andc.amway.datacubecatcher.wx.source.SourceManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by benjaminkc on 17/8/7.
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    SourceManager sourceManager;

    @ApiOperation(value = "获取所有公众号分派信息")
    @GetMapping
    public Response get(){
        List<Account> accounts = sourceManager.findAllAccounts();
        return Response.ok("accounts", accounts);
    }

    @ApiOperation(value = "图文群发总数据", notes = "指定msgid请求详细数据")
    @ApiImplicitParam(name = "ids", value = "公众号分派ID集合", required = true, dataType = "List<String>")
    @PostMapping
    public Response find(@RequestParam List<String> ids){
        List<Account> accounts =
                Optional.ofNullable(ids).isPresent() && !ids.isEmpty() ?
                    sourceManager.findAccountByIds(ids) :
                    sourceManager.findAllAccounts();
        return Response.ok("accounts", accounts);
    }

}
