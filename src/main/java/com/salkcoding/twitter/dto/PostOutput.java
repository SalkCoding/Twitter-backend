package com.salkcoding.twitter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostOutput {
    long postId;
    String writerId;
    String content;
    int postCommentCount;
    int postLikeCount;
    String created;
}
