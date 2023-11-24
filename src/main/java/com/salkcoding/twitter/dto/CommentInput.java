package com.salkcoding.twitter.dto;

import lombok.Data;

@Data
public class CommentInput {
    private long postId;
    private String content;
}
