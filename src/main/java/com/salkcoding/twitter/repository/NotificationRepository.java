package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "select * from `notification` where `user_id`=:userId",nativeQuery = true)
    List<Notification> findAllByUserId(String userId);

    @Modifying
    @Query(value = "delete from `notification` where `user_id`=:userId", nativeQuery = true)
    void deleteAllByUserId(String userId);

}
