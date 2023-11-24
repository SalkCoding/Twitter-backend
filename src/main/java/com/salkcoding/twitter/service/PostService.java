package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Follow;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.repository.FollowRepository;
import com.salkcoding.twitter.repository.PostLikeRepository;
import com.salkcoding.twitter.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CascadingService cascadingService;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;

    public List<Post> getReadablePostList(String readerId) {

        //Written by himself
        List<Post> list = new ArrayList<>(postRepository.findAllByWriterId(readerId));

        //Written by Followers
        List<Follow> followers = followRepository.getFollowsByFollowerId(readerId);
        followers.forEach(follow -> {
            list.addAll(postRepository.findAllByWriterId(follow.getTargetId()));
        });

        return list;
    }

    @Transactional
    public void addPost(String writerId, String content) {
        Post post = new Post(writerId, content);
        postRepository.save(post);
    }

    @Transactional
    public void deletePost(String userId, long postId) {
        Post targetPost = postRepository.findById(postId).orElse(null);
        if (targetPost == null) return;
        if (!targetPost.getWriterId().equals(userId)) return;

        cascadingService.removePostCascading(postId);
    }

    public Post getPostById(long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getLikedPostList(String likerId) {
        List<Post> postList = new ArrayList<>();
        var list = postLikeRepository.findAllByLikerId(likerId);
        list.forEach(postLike -> {
            Post post = getPostById(postLike.getPostId());
            postList.add(post);
        });
        return postList;
    }

    public int countPostWritten(String userId){
        return postRepository.countPostByUserId(userId);
    }
}
