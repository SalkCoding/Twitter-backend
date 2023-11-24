package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Follow;
import com.salkcoding.twitter.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    public boolean isFollowed(String targetId, String followerId) {
        return followRepository.findByFollowerIdAndTargetId(targetId, followerId) != null;
    }

    public List<Follow> getFollowList(String userId){
        return followRepository.getFollowsByFollowerId(userId);
    }

    public List<Follow> getFollowerList(String targetId){
        return followRepository.getFollowersByTargetId(targetId);
    }

    @Transactional
    public void addFollow(String targetId, String followerId) {
        Follow follow = new Follow(targetId, followerId);
        followRepository.save(follow);
    }

    @Transactional
    public void deleteFollow(String targetId, String followerId) {
        followRepository.deleteByTargetIdAndFollowerId(targetId,followerId);
    }

}
