package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query(value = "select * from `block` where `blocker_id`=:blockerId", nativeQuery = true)
    List<Block> findAllByBlockerId(String blockerId);

    @Query(value = "select * from `block` where `target_id`=:targetId", nativeQuery = true)
    List<Block> findAllByTargetId(String targetId);

    @Query(value = "select *  from `block` where `target_id`=:targetId and `blocker_id`=:blockerId", nativeQuery = true)
    Block findBlockByTargetIdAndBlockerId(String targetId, String blockerId);

    @Modifying
    @Query(value = "delete from `block` where `target_id`=:targetId and `blocker_id`=:blockerId", nativeQuery = true)
    void deleteAllByTargetIdAndBlockerId(String targetId, String blockerId);

    @Modifying
    @Query(value = "delete from `block` where `blocker_id`=:blockerId", nativeQuery = true)
    void deleteAllByBlockerId(String blockerId);

    @Modifying
    @Query(value = "delete from `block` where `target_id`=:targetId", nativeQuery = true)
    void deleteAllByTargetId(String targetId);
}
