package com.salkcoding.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockOutput {

    long blockId;
    String targetId;
    String blockerId;
    String created;

    String lastLogin;

}
