package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.CommentOutput;
import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.CommentLikeService;
import com.salkcoding.twitter.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class LikedCommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/liked/comment")
    public String showCommentPage(
            @SessionAttribute(value = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        Calendar calendar = Calendar.getInstance();
        List<CommentOutput> commentOutputList = new ArrayList<>();
        List<Comment> commentList = commentService.getLikedCommentList(user.getUserId());
        commentList.forEach(comment -> {
            comment.setContent(comment.getContent().replaceAll("\n", "<br>"));
            calendar.setTimeInMillis(comment.getCreated());
            commentOutputList.add(
                    new CommentOutput(
                            comment.getCommentId(),
                            comment.getWriterId(),
                            comment.getContent(),
                            commentLikeService.getLikeCountOnComment(comment.getCommentId()),
                            comment.getPostId(),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("comment", commentOutputList);

        return "likedComment";
    }

}
