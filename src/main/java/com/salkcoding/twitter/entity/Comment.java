package com.salkcoding.twitter.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long commentId;

    long postId;

    String writerId;

    @Setter
    String content;

    long created;

    public Comment(long postId, String writerId, String content) {
        this.postId = postId;
        this.writerId = writerId;
        this.content = content;
        this.created = System.currentTimeMillis();
    }
}
