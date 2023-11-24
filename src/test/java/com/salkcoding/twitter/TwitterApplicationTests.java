package com.salkcoding.twitter;

import com.salkcoding.twitter.dto.LoginDTO;
import com.salkcoding.twitter.entity.Post;
import com.salkcoding.twitter.repository.PostRepository;
import com.salkcoding.twitter.service.PostService;
import com.salkcoding.twitter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TwitterApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Test
    @Transactional
    void loginTest() {
        Post post = new Post();
        postRepository.save(post);
        System.out.println(postRepository.findAll());
    }

}
