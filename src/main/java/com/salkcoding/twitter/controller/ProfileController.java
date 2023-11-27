package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.CommentOutput;
import com.salkcoding.twitter.dto.PasswordDTO;
import com.salkcoding.twitter.dto.PostOutput;
import com.salkcoding.twitter.dto.ProfileOutput;
import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final FollowService followService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/profile")
    public String profilePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        int followerCnt = followService.getFollowerList(user.getUserId()).size();
        ProfileOutput profile = new ProfileOutput(
                user.getUserId(),
                user.getPassword(),
                postService.countPostWritten(user.getUserId()),
                commentService.countCommentWritten(user.getUserId()),
                followerCnt == 0 ? "No followers :(" : String.valueOf(followerCnt),
                user.getLastLogin()
        );
        model.addAttribute("profile", profile);

        Calendar calendar = Calendar.getInstance();
        List<PostOutput> tweetList = new ArrayList<>();
        List<Post> postList = postService.getPostsWrittenByUser(user.getUserId());
        postList.forEach(post -> {
            post.setContent(post.getContent().replaceAll("\n", "<br>"));
            calendar.setTimeInMillis(post.getCreated());
            tweetList.add(
                    new PostOutput(
                            post.getPostId(),
                            post.getWriterId(),
                            post.getContent(),
                            commentService.countCommentOnPost(post.getPostId()),
                            postLikeService.getLikeCountOnPost(post.getPostId()),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("tweet", tweetList);

        List<CommentOutput> commentOutputList = new ArrayList<>();
        List<Comment> commentList = commentService.getCommentsWrittenByUser(user.getUserId());
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

        return "profile";
    }

}
