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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long postId;

    String writerId;

    @Setter
    String content;

    long created;

    public Post(String writerId, String content) {
        this.writerId = writerId;
        this.content = content;
        this.created = System.currentTimeMillis();
    }

}
