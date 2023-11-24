package com.salkcoding.twitter.service;

import com.salkcoding.twitter.dto.LoginDTO;
import com.salkcoding.twitter.entity.User;
import com.salkcoding.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CascadingService cascadingService;

    private final UserRepository userRepository;

    public boolean isRegisteredUser(String userId, String password) {
        return userRepository.findUserByIdAndPassword(userId, password) != null;
    }

    public boolean isRegisteredUserId(String userId) {
        return userRepository.existsById(userId);
    }

    @Transactional
    public User signUpUser(String userId, String password) {
        User user = new User(userId, password);
        userRepository.save(user);
        return user;
    }

    public User getUser(LoginDTO loginDTO) {
        return getUser(loginDTO.getUserId(), loginDTO.getPassword());
    }

    public User getUser(String userId, String password) {
        return userRepository.findUserByIdAndPassword(userId, password);
    }

    public long getUserLastLogin(String userId) {
        return userRepository.findById(userId).orElseThrow().getLastLogin();
    }

    @Transactional
    public void updateLastLogin(String userId) {
        userRepository.updateLastLoginByUserId(userId, System.currentTimeMillis());
    }

    @Transactional
    public void changePassword(String userId, String oldPassword, String newPassword) {
        if (getUser(userId, oldPassword) != null)
            userRepository.updatePasswordByUserId(userId, oldPassword, newPassword);
    }

    @Transactional
    public void deleteUser(String userId, String password) {
        if (getUser(userId, password) != null)
            cascadingService.removeUserCascading(userId);
    }

}
