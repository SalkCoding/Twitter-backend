package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.PasswordDTO;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class MoreController {

    private final UserService userService;

    @GetMapping("/more")
    public String morePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        return "more";
    }

    @PostMapping("/password/change")
    public String changePasswordPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest servletRequest,
            PasswordDTO passwordDTO
    ) {
        if (user == null) return "redirect:/login";

        userService.changePassword(user.getUserId(), user.getPassword(), passwordDTO.getPassword());

        servletRequest.getSession().invalidate();

        return "redirect:/login";
    }

    @PostMapping("/user/delete")
    public String deleteUserPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest servletRequest
    ) {
        if (user == null) return "redirect:/login";

        servletRequest.getSession().invalidate();

        userService.deleteUser(user.getUserId(), user.getPassword());

        return "redirect:/login";
    }

}
