package com.salkcoding.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentOutput {
    long commentId;
    String writerId;
    String content;

    int commentLikeCount;
    long postId;
}
