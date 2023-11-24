package com.salkcoding.twitter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long followId;

    String targetId;

    String followerId;

    public Follow(String targetId, String followerId) {
        this.targetId = targetId;
        this.followerId = followerId;
    }
}
