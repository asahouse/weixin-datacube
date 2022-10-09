package com.andc.amway.datacubecatcher.filter;

import com.andc.amway.datacubecatcher.service.UserManager;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Deque;
import java.util.Map;

@Log
@Component
public class ApiFilter extends OncePerRequestFilter {

    @Autowired
    UserManager userManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath =
                ((HttpServletRequestImpl) httpServletRequest).getExchange().getRequestPath();
        Map<String, Deque<String>> querys =
                ((HttpServletRequestImpl) httpServletRequest).getExchange().getQueryParameters();

        //下载使用URL参数传递token
        if (requestPath.indexOf("/api/download/")!=-1
                && querys.containsKey("token")
                && userManager.haslogin(querys.get("token").getFirst())){
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }else {
            String token = httpServletRequest.getHeader("token");
            if (userManager.haslogin(token)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                RequestDispatcher dispatcher = httpServletRequest.getRequestDispatcher("/user/error");
                dispatcher.forward(httpServletRequest, httpServletResponse);
            }
        }

        return;
    }
}
