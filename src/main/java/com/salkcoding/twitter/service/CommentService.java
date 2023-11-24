package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.CommentLike;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.repository.CommentLikeRepository;
import com.salkcoding.twitter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CascadingService cascadingService;

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    public boolean hasComment(long postId) {
        List<Comment> commentList = commentRepository.getCommentsByPostId(postId);
        if (commentList == null) return false;
        return !commentList.isEmpty();
    }

    @Transactional
    public void addComment(long postId, String writerId, String content) {
        Comment comment = new Comment(postId, writerId, content);
        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(long commentId, String userId){
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if(comment.getWriterId().equals(userId))
            cascadingService.removeCommentCascading(commentId);
    }

    public List<Comment> getCommentListOnPost(long postId) {
        return commentRepository.getCommentsByPostId(postId);
    }

    public List<Comment> getLikedCommentList(String likerId) {
        List<Comment> commentList = new ArrayList<>();
        commentLikeRepository.findAllByLikerId(likerId).forEach(commentLike -> {
            Comment comment = commentRepository.findById(commentLike.getCommentId()).orElse(null);
            commentList.add(comment);
        });
        return commentList;
    }

    public int countCommentWritten(String userId) {
        return commentRepository.countByWriterId(userId);
    }
}
