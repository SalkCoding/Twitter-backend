package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = "select * from `comment` where `post_id`=:postId order by `created` desc", nativeQuery = true)
    List<Comment> getCommentsByPostId(long postId);

    @Query(value = "select * from `comment` where `writer_id`=:writerId order by `created` desc", nativeQuery = true)
    List<Comment> findAllByWriterId(String writerId);

    @Query(value = "select count(*) from `comment` where `writer_id`=:writerId", nativeQuery = true)
    int countByWriterId(String writerId);

    @Modifying
    @Query(value = "delete from `comment` where `writer_id`=:writerId", nativeQuery = true)
    void deleteAllByWriterId(String writerId);

    @Modifying
    @Query(value = "delete from `comment` where `post_id`=:postId", nativeQuery = true)
    void deleteAllByPostId(long postId);

}
