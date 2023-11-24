package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Notification;
import com.salkcoding.twitter.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    public void addNotification(String userId, String content) {
        if (content.isBlank() || content.isEmpty()) return;
        if (!userService.isRegisteredUserId(userId)) return;

        Notification notification = new Notification(userId, content);
        notificationRepository.save(notification);
    }

    public void deleteNotification(long notificationId){
        notificationRepository.deleteById(notificationId);
    }

    public List<Notification> getNotificationListOnUser(String userId){
        return notificationRepository.findAllByUserId(userId);
    }

}
