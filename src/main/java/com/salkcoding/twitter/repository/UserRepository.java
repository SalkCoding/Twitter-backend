package com.salkcoding.twitter.repository;

import com.salkcoding.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "select * from `user` where `user_id`=:userId and `password`=:userPassword", nativeQuery = true)
    User findUserByIdAndPassword(String userId, String userPassword);

    @Modifying
    @Query(value = "update `user` set `last_login`=:lastLogin where `user_id`=:userId", nativeQuery = true)
    void updateLastLoginByUserId(String userId, long lastLogin);

    @Modifying
    @Query(value = "update `user` set `password`=:newPassword where `user_id`=:userId and `password`=:oldPassword", nativeQuery = true)
    void updatePasswordByUserId(String userId, String oldPassword, String newPassword);

}
