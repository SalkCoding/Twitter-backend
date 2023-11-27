package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.FollowDTO;
import com.salkcoding.twitter.dto.FollowInfoDTO;
import com.salkcoding.twitter.dto.ProfileOutput;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.FollowService;
import com.salkcoding.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FollowController {

    private final UserService userService;
    private final FollowService followService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/follow")
    public String communityPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            @ModelAttribute("errorMessage") String message,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        if (message != null && !message.isEmpty() && !message.isBlank())
            model.addAttribute("failMessage", message);

        List<FollowInfoDTO> followList = new ArrayList<>();
        followService.getFollowList(user.getUserId()).forEach(follow -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(userService.getUserLastLogin(follow.getTargetId()));
            followList.add(
                    new FollowInfoDTO(
                            follow.getTargetId(),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("follow", followList);

        List<FollowInfoDTO> followerList = new ArrayList<>();
        followService.getFollowerList(user.getUserId()).forEach(follow->{
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(userService.getUserLastLogin(follow.getFollowerId()));
            followerList.add(
                    new FollowInfoDTO(
                            follow.getFollowerId(),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("follower", followerList);

        return "follow";
    }

    @PostMapping("/follow/create")
    public String followPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            RedirectAttributes redirectAttributes,
            HttpServletRequest httpServletRequest,
            FollowDTO followDTO
    ) {
        if (user == null) return "redirect:/login";

        String targetId = followDTO.getTargetId();
        String userId = user.getUserId();

        if (!userService.isRegisteredUserId(targetId)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid user ID!");
            return "redirect:/follow";
        }

        if (!followService.isFollowed(targetId, userId))
            followService.addFollow(targetId, userId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/follow/delete")
    public String followDeletePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            FollowDTO followDTO
    ) {
        if (user == null) return "redirect:/login";

        String targetId = followDTO.getTargetId();
        String userId = user.getUserId();
        if (followService.isFollowed(targetId, userId))
            followService.deleteFollow(targetId, userId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

}
