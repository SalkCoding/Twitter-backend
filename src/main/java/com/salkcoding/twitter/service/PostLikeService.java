package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.PostLike;
import com.salkcoding.twitter.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;

    @Transactional
    public void addPostLike(long postId, String likerId) {
        PostLike postLike = new PostLike(postId,likerId);
        postLikeRepository.save(postLike);
    }

    @Transactional
    public void deletePostLike(long postId, String likerId) {
        postLikeRepository.deleteByPostIdAndLikerId(postId,likerId);
    }

    public boolean isLikedPost(long postId, String likerId) {
        PostLike postLike = postLikeRepository.findByPostIdAndLikerId(postId, likerId);
        return postLike != null;
    }

    public int getLikeCountOnPost(long postId){
        return postLikeRepository.countPostLikeByPostId(postId);
    }

}
