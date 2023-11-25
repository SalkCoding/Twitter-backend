package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.CommentLike;
import com.salkcoding.twitter.repository.CommentLikeRepository;
import com.salkcoding.twitter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    private final NotificationService notificationServicel;

    @Transactional
    public void addCommentLike(long commentId, String likerId) {
        CommentLike commentLike = new CommentLike(commentId, likerId);

        Comment comment = commentRepository.findById(commentId).orElseThrow();

        if (!comment.getWriterId().equals(likerId))
            notificationServicel.addNotification(comment.getWriterId(), "@" + likerId + " likes your comment! [" + comment.getContent() + "]");

        commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(long commentId, String likerId) {
        commentLikeRepository.deleteByCommentIdAndLikerId(commentId, likerId);
    }

    public boolean isLikedComment(long commentId, String likerId) {
        CommentLike commentLike = commentLikeRepository.findByCommentIdAndLikerId(commentId, likerId);
        return commentLike != null;
    }

    public int getLikeCountOnComment(long postId) {
        return commentLikeRepository.countCommentLikeByCommentId(postId);
    }

}
