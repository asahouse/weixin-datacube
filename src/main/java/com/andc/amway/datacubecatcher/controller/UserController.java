package com.andc.amway.datacubecatcher.controller;

import com.andc.amway.datacubecatcher.service.UserManager;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserManager userManager;

    @GetMapping("error")
    public Response error(){
        return Response.resp(HttpStatus.UNAUTHORIZED,"未授权禁止请求");
    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "账户名称", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @PostMapping("login")
    public Response login(@RequestParam String name,
                          @RequestParam String password){

        if (StringUtils.isEmpty(name) ||
                StringUtils.isEmpty(password))
            return Response.resp(HttpStatus.BAD_REQUEST, "name & password can not empty!");

        Optional<String> token = userManager.login(name, password);
        if (!token.isPresent())
            return Response.resp(HttpStatus.UNAUTHORIZED, "name & password error!");

        return Response.ok("token", token.get());
    }

    @ApiOperation(value = "用户登出")
    @ApiImplicitParam(name = "token", value = "登录token", required = true, dataType = "List<String>")
    @GetMapping("logout")
    public Response logout(@RequestParam String token){
        userManager.logout(token);
        return Response.ok();
    }
}
