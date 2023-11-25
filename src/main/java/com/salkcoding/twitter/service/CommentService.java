package com.salkcoding.twitter.service;

import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.repository.CommentLikeRepository;
import com.salkcoding.twitter.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CascadingService cascadingService;

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final NotificationService notificationService;

    public boolean hasComment(long postId) {
        List<Comment> commentList = commentRepository.getCommentsByPostId(postId);
        if (commentList == null) return false;
        return !commentList.isEmpty();
    }

    private final Pattern mentionPattern = Pattern.compile("@\\w+");

    @Transactional
    public void addComment(long postId, String writerId, String content) {
        Comment comment = new Comment(postId, writerId, content);

        //Mention detect
        Matcher matcher = mentionPattern.matcher(content);
        while (matcher.find()) {
            String findId = matcher.group().substring(1);
            //Wrong userId
            if (!userService.isRegisteredUserId(findId)) continue;

            if (!comment.getWriterId().equals(findId))
                notificationService.addNotification(findId, "@" + writerId + " mentioned you in the comment! [" + content + "]");
        }

        commentRepository.save(comment);
    }

    @Transactional
    public void removeComment(long commentId, String userId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        if (comment.getWriterId().equals(userId))
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

        commentList.sort(Comparator.comparingLong(Comment::getCreated));
        Collections.reverse(commentList);

        return commentList;
    }

    public int countCommentWritten(String userId) {
        return commentRepository.countByWriterId(userId);
    }
}
