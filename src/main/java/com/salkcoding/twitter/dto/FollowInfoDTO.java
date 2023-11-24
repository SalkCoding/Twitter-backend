package com.salkcoding.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FollowInfoDTO {
    String targetId;
    String lastLogin;
}
