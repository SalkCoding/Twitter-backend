package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.*;
import com.salkcoding.twitter.entity.Comment;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.CommentLikeService;
import com.salkcoding.twitter.service.CommentService;
import com.salkcoding.twitter.service.PostLikeService;
import com.salkcoding.twitter.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final PostService postService;
    private final PostLikeService postLikeService;
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/comment")
    public String showCommentPage(
            @SessionAttribute(value = "loginUser", required = false) User user,
            @ModelAttribute("postId") long postId,
            Model model
    ) {
        if (user == null) return "redirect:/login";

        Post post = postService.getPostById(postId);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(post.getCreated());
        PostOutput tweet = new PostOutput(
                post.getPostId(),
                post.getWriterId(),
                post.getContent(),
                postLikeService.getLikeCountOnPost(post.getPostId()),
                simpleDateFormat.format(calendar.getTime())
        );
        model.addAttribute("tweet", tweet);

        if (!commentService.hasComment(postId)) {
            model.addAttribute("failMessage", "No comments on this post!");
            model.addAttribute("postId", postId);
            return "comment";
        }

        List<CommentOutput> commentOutputList = new ArrayList<>();
        List<Comment> commentList = commentService.getCommentListOnPost(postId);
        commentList.forEach(comment -> {
            comment.setContent(comment.getContent().replaceAll("\n", "<br>"));
            calendar.setTimeInMillis(comment.getCommentId());
            commentOutputList.add(
                    new CommentOutput(
                            comment.getCommentId(),
                            comment.getWriterId(),
                            comment.getContent(),
                            commentLikeService.getLikeCountOnComment(comment.getCommentId()),
                            postId,
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("comment", commentOutputList);
        model.addAttribute("postId", postId);

        return "comment";
    }

    @PostMapping("/comment/create")
    public String addCommentPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            CommentInput commentInput,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {
        if (user == null) return "redirect:/login";

        long postId = commentInput.getPostId();
        String content = commentInput.getContent();
        String userId = user.getUserId();

        redirectAttributes.addFlashAttribute("postId", postId);

        if (content.isEmpty() || content.isBlank()) return "redirect:/comment";

        commentService.addComment(postId, userId, content);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/comment/delete")
    public String deleteCommentPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            CommentLikeDTO commentDTO,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {
        if (user == null) return "redirect:/login";

        commentService.removeComment(commentDTO.getCommentId(), user.getUserId());

        redirectAttributes.addFlashAttribute("postId", commentDTO.getPostId());

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/comment/like")
    public String likeCommentPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            CommentLikeDTO commentDTO,
            HttpServletRequest httpServletRequest,
            RedirectAttributes redirectAttributes
    ) {
        if (user == null) return "redirect:/login";

        long commentId = commentDTO.getCommentId();
        String userId = user.getUserId();
        if (commentLikeService.isLikedComment(commentId, userId))
            commentLikeService.deleteCommentLike(commentId, userId);
        else
            commentLikeService.addCommentLike(commentId, userId);

        redirectAttributes.addFlashAttribute("postId", commentDTO.getPostId());

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/comment/like/cancel")
    public String likeCancelPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            CommentLikeDTO commentLikeDTO
    ) {
        if (user == null) return "redirect:/login";

        long commentId = commentLikeDTO.getCommentId();
        String userId = user.getUserId();
        if (commentLikeService.isLikedComment(commentId, userId))
            commentLikeService.deleteCommentLike(commentId, userId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

}
