package com.salkcoding.twitter.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logOutPage(HttpServletRequest servletRequest) {
        servletRequest.getSession().invalidate();
        return "redirect:/login";
    }

}
