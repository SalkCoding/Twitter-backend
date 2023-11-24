package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.NotificationInput;
import com.salkcoding.twitter.dto.NotificationOutput;
import com.salkcoding.twitter.entity.Notification;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notification")
    public String notificationPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        List<NotificationOutput> outputList = new ArrayList<>();
        List<Notification> notificationList = notificationService.getNotificationListOnUser(user.getUserId());
        notificationList.forEach(notification -> {
            notification.setContent(notification.getContent().replaceAll("\n", "<br>"));
            outputList.add(
                    new NotificationOutput(
                            notification.getNotificationId(),
                            notification.getContent(),
                            notification.getCreated()
                    )
            );
        });

        if(outputList.isEmpty())
            model.addAttribute("infoMessage","There are no notifications received!");

        model.addAttribute("notification", outputList);

        return "notification";
    }

    @PostMapping("/notification/delete")
    public String notificationDeletePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            NotificationInput notificationInput
    ) {
        if (user == null) return "redirect:/login";

        notificationService.deleteNotification(notificationInput.getNotificationId());

        return "redirect:/notification";
    }
}
