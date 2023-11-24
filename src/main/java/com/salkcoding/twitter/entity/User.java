package com.salkcoding.twitter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {
    @Id
    @Column(unique = true)
    String userId;

    String password;

    long lastLogin;

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
        this.lastLogin = System.currentTimeMillis();
    }
}
