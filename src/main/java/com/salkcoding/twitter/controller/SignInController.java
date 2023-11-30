package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.LoginDTO;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignInController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(
            @SessionAttribute(value = "loginUser", required = false) User user
    ) {
        if (user != null) return "redirect:http://localhost:8080/";
        return "login";
    }

    @PostMapping("/signin")
    public String signInPage(
            @SessionAttribute(value = "loginUser", required = false) User user,
            HttpServletRequest servletRequest,
            LoginDTO loginDTO,
            Model model
    ) {
        if (user != null) return "redirect:http://localhost:8080/";

        //로그인 성공
        if (!userService.isRegisteredUser(loginDTO.getUserId(), loginDTO.getPassword())) {
            //model에 alter 추가
            model.addAttribute("failMessage", "Invalid user ID or password!");
            return "login";
        }

        User loginResult = userService.getUser(loginDTO);
        assert loginResult != null;

        servletRequest.getSession().invalidate();
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("loginUser", loginResult);

        userService.updateLastLogin(loginResult.getUserId());

        return "redirect:http://localhost:8080/";
    }
}
