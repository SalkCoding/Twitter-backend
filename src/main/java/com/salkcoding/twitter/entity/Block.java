package com.salkcoding.twitter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Block {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long blockId;

    String targetId;

    String blockerId;

    long blockedTime;

    public Block(String targetId, String blockerId) {
        this.targetId = targetId;
        this.blockerId = blockerId;
        this.blockedTime = System.currentTimeMillis();
    }
}
