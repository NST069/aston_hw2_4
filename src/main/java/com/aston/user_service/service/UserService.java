package com.aston.user_service.service;

import com.aston.user_service.exception.UserAlreadyExistsException;
import com.aston.user_service.exception.UserNotFoundException;
import com.aston.user_service.model.User;
import com.aston.user_service.repository.UserJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService {

    private final UserJPARepository userJPARepository;

    @Autowired
    public UserService(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    public User createUser(User user) {
        if(this.userJPARepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException(user.getEmail());
        return this.userJPARepository.save(user);
    }

    public User findUserById(String userId) {
        return this.userJPARepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User updateUser(String userId, User user) {
        return this.userJPARepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setName(user.getName());
                    oldUser.setEmail(user.getEmail());
                    oldUser.setAge(user.getAge());

                    return this.userJPARepository.save(oldUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    public void deleteUser(String userId) {
        this.userJPARepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        this.userJPARepository.deleteById(userId);
    }
}
