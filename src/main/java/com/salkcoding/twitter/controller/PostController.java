package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.PostInput;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.PostLikeService;
import com.salkcoding.twitter.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    @PostMapping("/post/create")
    public String addPostPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            PostInput postInput
    ) {
        if (user == null) return "redirect:/login";

        if (postInput.getContent().isEmpty()) return "redirect:/";

        postService.addPost(user.getUserId(), postInput.getContent());

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/post/delete")
    public String deletePostPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            PostInput postInput
    ) {
        if (user == null) return "redirect:/login";

        postService.deletePost(user.getUserId(), postInput.getPostId());
        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/post/like")
    public String likePostPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            PostInput PostOutput
    ) {
        if (user == null) return "redirect:/login";

        long postId = PostOutput.getPostId();
        String userId = user.getUserId();
        if (postLikeService.isLikedPost(postId, userId))
            postLikeService.deletePostLike(postId, userId);
        else
            postLikeService.addPostLike(postId, userId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

    @PostMapping("/post/like/cancel")
    public String likeCancelPage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            HttpServletRequest httpServletRequest,
            PostInput PostOutput
    ) {
        if (user == null) return "redirect:/login";

        long postId = PostOutput.getPostId();
        String userId = user.getUserId();
        if (postLikeService.isLikedPost(postId, userId))
            postLikeService.deletePostLike(postId, userId);

        return "redirect:" + httpServletRequest.getHeader("Referer");
    }

}
