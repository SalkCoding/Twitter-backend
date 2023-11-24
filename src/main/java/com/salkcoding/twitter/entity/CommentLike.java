package com.salkcoding.twitter.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long commentLikeId;

    long commentId;

    String likerId;

    public CommentLike(long commentId, String likerId) {
        this.commentId = commentId;
        this.likerId = likerId;
    }

}
