package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(value = "select * from `post` where `writer_id`=:writerId", nativeQuery = true)
    List<Post> findAllByWriterId(String writerId);

    @Modifying
    @Query(value = "delete from `post` where `writer_id`=:writerId", nativeQuery = true)
    void deleteAllByWriterId(String writerId);

    @Query(value = "select count(*) from `post` where `writer_id`=:userId", nativeQuery = true)
    int countPostByUserId(String userId);

}
