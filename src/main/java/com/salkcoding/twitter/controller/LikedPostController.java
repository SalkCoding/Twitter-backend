package com.salkcoding.twitter.controller;

import com.salkcoding.twitter.dto.PostOutput;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.service.PostLikeService;
import com.salkcoding.twitter.service.PostService;
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
public class LikedPostController {

    private final PostService postService;
    private final PostLikeService postLikeService;

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/liked/post")
    public String postLikePage(
            @SessionAttribute(name = "loginUser", required = false) User user,
            Model model
    ){
        if (user == null) return "redirect:/login";

        Calendar calendar = Calendar.getInstance();
        List<PostOutput> tweetList = new ArrayList<>();
        List<Post> postList = postService.getLikedPostList(user.getUserId());
        postList.forEach(post -> {
            post.setContent(post.getContent().replaceAll("\n", "<br>"));
            calendar.setTimeInMillis(post.getCreated());
            tweetList.add(
                    new PostOutput(
                            post.getPostId(),
                            post.getWriterId(),
                            post.getContent(),
                            postLikeService.getLikeCountOnPost(post.getPostId()),
                            simpleDateFormat.format(calendar.getTime())
                    )
            );
        });
        model.addAttribute("tweet", tweetList);

        return "likedPost";
    }
}
