package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.algorithm.KnowablePersonFinder;
import com.salkcoding.twitter.dto.FollowInfoDTO;
import com.salkcoding.twitter.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ExploreController {

    private final KnowablePersonFinder finder;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/explore")
    public String explorePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        Calendar calendar = Calendar.getInstance();
        List<FollowInfoDTO> userList = new ArrayList<>();
        finder.calculateKnowableUserList(user.getUserId()).forEach(u -> {
            calendar.setTimeInMillis(u.getLastLogin());
            userList.add(
                    new FollowInfoDTO(
                            u.getUserId(),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("user", userList);

        return "explore";
    }

}
