package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.LoginDTO;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class SignUpController {

    private final UserService userService;

    @GetMapping("/signup")
    public String loginPage(
            @SessionAttribute(value = "loginUser", required = false) User user
    ) {
        if (user != null) return "redirect:http://localhost:8080/";
        return "register";
    }

    @PostMapping("/register")
    public String signPage(
            @SessionAttribute(value = "loginUser", required = false) User user,
            HttpServletRequest servletRequest,
            LoginDTO loginDTO,
            Model model
    ) {
        if (user != null) return "redirect:http://localhost:8080/";

        if (userService.isRegisteredUserId(loginDTO.getUserId())) {
            //model에 alter 추가
            model.addAttribute("failMessage", "That user ID is already used!");
            return "register";
        }

        //로그인 성공
        User loginResult = userService.signUpUser(loginDTO.getUserId(), loginDTO.getPassword());
        assert loginResult != null;

        servletRequest.getSession().invalidate();
        HttpSession session = servletRequest.getSession(true);
        session.setAttribute("loginUser", loginResult);

        return "redirect:http://localhost:8080/";
    }

}
