package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.CommentLike;
import com.salkcoding.twitter.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void addCommentLike(long commentId, String likerId) {
        CommentLike commentLike = new CommentLike(commentId,likerId);
        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(long commentId, String likerId) {
        commentLikeRepository.deleteByCommentIdAndLikerId(commentId,likerId);
    }

    public boolean isLikedComment(long commentId, String likerId) {
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndLikerId(commentId, likerId);
        return commentLike != null;
    }

    public int getLikeCountOnComment(long postId){
        return commentLikeRepository.countCommentLikeByCommentId(postId);
    }

}
