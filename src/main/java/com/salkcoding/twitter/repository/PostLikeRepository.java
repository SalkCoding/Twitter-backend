package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    @Query(value = "select * from `post_like` where `post_id`=:postId and `liker_id`=:likerId", nativeQuery = true)
    PostLike findByPostIdAndLikerId(long postId, String likerId);

    @Query(value = "select * from `post_like` where `liker_id`=:likerId", nativeQuery = true)
    List<PostLike> findAllByLikerId(String likerId);

    @Modifying
    @Query(value = "delete from `post_like` where `post_id`=:postId and `liker_id`=:likerId", nativeQuery = true)
    void deleteByPostIdAndLikerId(long postId, String likerId);

    @Modifying
    @Query(value = "delete from `post_like` where `liker_id`=:likerId", nativeQuery = true)
    void deleteAllByLikerId(String likerId);

    @Query(value = "select count(*) from `post_like` where `post_id`=:postId", nativeQuery = true)
    int countPostLikeByPostId(long postId);

    @Modifying
    @Query(value = "delete from `post_like` where `post_id`=:postId", nativeQuery = true)
    void deleteAllByPostId(long postId);
}
