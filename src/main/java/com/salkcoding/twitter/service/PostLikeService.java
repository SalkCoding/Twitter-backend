package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.entity.PostLike;
import com.salkcoding.twitter.repository.PostLikeRepository;
import com.salkcoding.twitter.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final NotificationService notificationService;

    @Transactional
    public void addPostLike(long postId, String likerId) {
        PostLike postLike = new PostLike(postId, likerId);

        Post post = postRepository.findById(postId).orElseThrow();
        if (!post.getWriterId().equals(likerId))
            notificationService.addNotification(post.getWriterId(), "@" + likerId + " likes your post! [" + post.getContent() + "]");

        postLikeRepository.save(postLike);
    }

    @Transactional
    public void deletePostLike(long postId, String likerId) {
        postLikeRepository.deleteByPostIdAndLikerId(postId, likerId);
    }

    public boolean isLikedPost(long postId, String likerId) {
        PostLike postLike = postLikeRepository.findByPostIdAndLikerId(postId, likerId);
        return postLike != null;
    }

    public int getLikeCountOnPost(long postId) {
        return postLikeRepository.countPostLikeByPostId(postId);
    }

}
