package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query(value = "select * from `follow` where `follower_id`=:followerId", nativeQuery = true)
    List<Follow> getFollowsByFollowerId(String followerId);

    @Query(value = "select * from `follow` where `target_id`=:targetId", nativeQuery = true)
    List<Follow> getFollowersByTargetId(String targetId);

    @Query(value = "select * from `follow` where `target_id`=:targetId and `follower_id`=:followerId", nativeQuery = true)
    Follow findByFollowerIdAndTargetId(String targetId, String followerId);

    @Modifying
    @Query(value = "delete from `follow` where `target_id`=:targetId and `follower_id`=:followerId", nativeQuery = true)
    void deleteByTargetIdAndFollowerId(String targetId, String followerId);

    @Modifying
    @Query(value = "delete from `follow` where `target_id`=:userId or `follower_id`=:userId", nativeQuery = true)
    void deleteAllByTargetIdOrFollowerId(String userId);

}
