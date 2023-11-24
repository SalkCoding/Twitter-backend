package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.PasswordDTO;
import com.salkcoding.twitter.dto.ProfileOutput;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.CommentService;
import com.salkcoding.twitter.service.FollowService;
import com.salkcoding.twitter.service.PostService;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;
    private final FollowService followService;

    @GetMapping("/profile")
    public String profilePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        List<String> followerList = new ArrayList<>();
        followService.getFollowerList(user.getUserId()).forEach(follow -> followerList.add(follow.getFollowerId()));
        ProfileOutput profile = new ProfileOutput(
                user.getUserId(),
                user.getPassword(),
                postService.countPostWritten(user.getUserId()),
                commentService.countCommentWritten(user.getUserId()),
                followerList.isEmpty() ? "No followers :(" : String.join(", ", followerList),
                user.getLastLogin()
        );
        model.addAttribute("profile", profile);

        return "profile";
    }

    @PostMapping("/profile/password/change")
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

}
