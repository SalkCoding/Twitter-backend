package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Follow;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.repository.FollowRepository;
import com.salkcoding.twitter.repository.PostLikeRepository;
import com.salkcoding.twitter.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CascadingService cascadingService;

    private final UserService userService;
    private final NotificationService notificationService;
    private final BlockService blockService;

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final FollowRepository followRepository;

    public List<Post> getReadablePostList(String readerId) {

        //Written by himself
        Set<Post> set = new HashSet<>(postRepository.findAllByWriterId(readerId));

        //Written by Followers
        List<Follow> followers = followRepository.getFollowsByFollowerId(readerId);
        followers.forEach(follow -> set.addAll(postRepository.findAllByWriterId(follow.getTargetId())));

        //Remove posts from people who blocked reader
        blockService.getBlockerList(readerId).forEach(block -> {
            set.removeIf(post-> block.getBlockerId().equals(post.getWriterId()));
        });

        List<Post> list = new ArrayList<>(set);
        list.sort(Comparator.comparingLong(Post::getCreated));
        Collections.reverse(list);

        return list;
    }

    private final Pattern mentionPattern = Pattern.compile("@\\w+");

    @Transactional
    public void addPost(String writerId, String content) {
        Post post = new Post(writerId, content);

        //Mention detect
        Matcher matcher = mentionPattern.matcher(content);
        while (matcher.find()) {
            String findId = matcher.group().substring(1);
            //Wrong userId
            if (!userService.isRegisteredUserId(findId)) continue;

            if (!writerId.equals(findId))
                notificationService.addNotification(findId, "@" + writerId + " mentioned you in the post! [" + content + "]");
        }

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

    public int countPostWritten(String userId) {
        return postRepository.countPostByUserId(userId);
    }

    public List<Post> getPostsWrittenByUser(String userId){
        return postRepository.findAllByWriterId(userId);
    }
}
