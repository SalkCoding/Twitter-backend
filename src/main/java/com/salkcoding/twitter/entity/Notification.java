package com.salkcoding.twitter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long notificationId;

    String userId;

    @Setter
    String content;

    long created;

    public Notification(String userId, String content) {
        this.userId = userId;
        this.content = content;
        this.created = System.currentTimeMillis();
    }
}
