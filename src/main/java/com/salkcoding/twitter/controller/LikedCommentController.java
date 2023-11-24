package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.CommentOutput;
import com.salkcoding.twitter.dto.PostOutput;
import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.CommentLikeService;
import com.salkcoding.twitter.service.CommentService;
import com.salkcoding.twitter.service.PostLikeService;
import com.salkcoding.twitter.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikedCommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    @GetMapping("/liked/comment")
    public String showCommentPage(
            @SessionAttribute(value = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        List<CommentOutput> commentOutputList = new ArrayList<>();
        List<Comment> commentList = commentService.getLikedCommentList(user.getUserId());
        commentList.forEach(comment -> {
            comment.setContent(comment.getContent().replaceAll("\n", "<br>"));
            commentOutputList.add(
                    new CommentOutput(
                            comment.getCommentId(),
                            comment.getWriterId(),
                            comment.getContent(),
                            commentLikeService.getLikeCountOnComment(comment.getCommentId()),
                            comment.getPostId()
                    )
            );
        });
        model.addAttribute("comment", commentOutputList);

        return "likedComment";
    }

}
