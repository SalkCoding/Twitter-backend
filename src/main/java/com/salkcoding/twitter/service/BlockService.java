package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Block;
import com.salkcoding.twitter.repository.BlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final UserService userService;

    private final BlockRepository blockRepository;

    public List<Block> getBlockList(String blockerId) {
        List<Block> list = blockRepository.findAllByBlockerId(blockerId);
        list.sort(Comparator.comparingLong(Block::getBlockedTime));
        Collections.reverse(list);
        return list;
    }

    public List<Block> getBlockerList(String targetId){
        return blockRepository.findAllByTargetId(targetId);
    }

    public boolean isBlocked(String targetId, String blockerId) {
        return blockRepository.findBlockByTargetIdAndBlockerId(targetId, blockerId) != null;
    }

    @Transactional
    public boolean addBlock(String targetId, String blockerId) {
        if (targetId.equals(blockerId) || !userService.isRegisteredUserId(targetId)) return false;

        Block block = new Block(targetId, blockerId);
        blockRepository.save(block);
        return true;
    }

    @Transactional
    public void deleteBlock(long blockId) {
        blockRepository.deleteById(blockId);
    }

}
