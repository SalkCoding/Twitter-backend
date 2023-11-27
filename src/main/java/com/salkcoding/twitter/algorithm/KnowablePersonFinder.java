package com.salkcoding.twitter.algorithm;

import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.FollowService;
import com.salkcoding.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class KnowablePersonFinder {

    private final UserService userService;
    private final FollowService followService;

    public List<User> calculateKnowableUserList(String userId) {
        Set<User> userSet = new HashSet<>();
        Queue<User> queue = new LinkedList<>();
        //팔로잉 목록
        followService.getFollowList(userId).forEach(follow -> queue.add(userService.getUser(follow.getTargetId())));
        //팔로우 목록
        followService.getFollowerList(userId).forEach(follow -> queue.add(userService.getUser(follow.getFollowerId())));

        while (!queue.isEmpty()) {
            User user = queue.poll();
            //2촌 검색
            followService.getFollowList(user.getUserId()).forEach(follow -> userSet.add(userService.getUser(follow.getTargetId())));
            followService.getFollowerList(user.getUserId()).forEach(follow -> userSet.add(userService.getUser(follow.getFollowerId())));
        }

        //자기 자신 삭제
        userSet.removeIf(user -> user.getUserId().equals(userId) || followService.isFollowed(user.getUserId(), userId));

        return userSet.stream().toList();
    }

}
