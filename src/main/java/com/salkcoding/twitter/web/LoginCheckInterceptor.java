package com.salkcoding.twitter.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loginUser") == null) {
            //로그인으로 redirect
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location","/login");
            response.sendRedirect("/login");
            log.info("An user not sign-in redirected from " + request.getRequestURI() + " to, /login");
            return false;
        }
        return true;
    }
}
