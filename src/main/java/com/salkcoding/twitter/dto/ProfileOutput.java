package com.salkcoding.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileOutput {
    String userId;
    String password;
    int postCount;
    int commentCount;
    String followers;
    long lastLogin;
}
