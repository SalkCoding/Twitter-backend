package com.salkcoding.twitter.service;

import com.salkcoding.twitter.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CascadingService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationRepository notificationRepository;
    private final FollowRepository followRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public void removeUserCascading(String userId){
        userRepository.deleteById(userId);
        postRepository.deleteAllByWriterId(userId);
        postLikeRepository.deleteAllByLikerId(userId);
        notificationRepository.deleteAllByUserId(userId);
        followRepository.deleteAllByTargetIdOrFollowerId(userId);
        commentRepository.deleteAllByWriterId(userId);
        commentLikeRepository.deleteAllByLikerId(userId);
    }

    @Transactional
    public void removePostCascading(long postId){
        postRepository.deleteById(postId);
        postLikeRepository.deleteAllByPostId(postId);
        commentRepository.getCommentsByPostId(postId).forEach(comment -> commentLikeRepository.deleteById(comment.getCommentId()));
        commentRepository.deleteAllByPostId(postId);
    }

    @Transactional
    public void removeCommentCascading(long commentId){
        commentRepository.deleteById(commentId);
        commentLikeRepository.deleteAllByCommentId(commentId);
    }

}
