package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.CommentLike;
import com.salkcoding.twitter.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    @Query(value = "select * from `comment_like` where `comment_id`=:commentId and `liker_id`=:likerId", nativeQuery = true)
    CommentLike findByCommentIdAndLikerId(long commentId, String likerId);

    @Modifying
    @Query(value = "delete from `comment_like` where `comment_id`=:commentId and `liker_id`=:likerId", nativeQuery = true)
    void deleteByCommentIdAndLikerId(long commentId, String likerId);

    @Query(value = "select count(*) from `comment_like` where `comment_id`=:commentId", nativeQuery = true)
    int countCommentLikeByCommentId(long commentId);

    @Modifying
    @Query(value = "delete from `comment_like` where `comment_id`=:commentId", nativeQuery = true)
    void deleteAllByCommentId(long commentId);

    @Query(value = "select * from `comment_like` where `liker_id`=:likerId", nativeQuery = true)
    List<CommentLike> findAllByLikerId(String likerId);

    @Modifying
    @Query(value = "delete from `comment_like` where `liker_id`=:likerId", nativeQuery = true)
    void deleteAllByLikerId(String likerId);

}
