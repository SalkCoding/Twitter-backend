package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.Block;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, Long> {
}
