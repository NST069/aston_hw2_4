package com.aston.user_service.service;

import com.aston.user_service.exception.UserAlreadyExistsException;
import com.aston.user_service.exception.UserNotFoundException;
import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.mapper.UserDTOToUserConverter;
import com.aston.user_service.mapper.UserToUserDTOConverter;
import com.aston.user_service.model.User;
import com.aston.user_service.repository.UserJPARepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserJPARepository userJPARepository;

    private final UserToUserDTOConverter userToUserDTOConverter = new UserToUserDTOConverter();

    private final UserDTOToUserConverter userDTOToUserConverter = new UserDTOToUserConverter();

    public UserService(UserJPARepository userJPARepository) {
        this.userJPARepository = userJPARepository;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = this.userDTOToUserConverter.convert(userDTO);
        if (this.userJPARepository.findByEmail(user.getEmail()).isPresent())
            throw new UserAlreadyExistsException(user.getEmail());
        return this.userToUserDTOConverter.convert(this.userJPARepository.save(user));
    }

    @Transactional
    public UserDTO findUserById(String userId) {
        User foundUser = this.userJPARepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        return this.userToUserDTOConverter.convert(foundUser);
    }

    @Transactional
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        User user = this.userDTOToUserConverter.convert(userDTO);
        User updatedUser = this.userJPARepository.findById(userId)
                .map(oldUser -> {
                    oldUser.setName(user.getName());
                    oldUser.setEmail(user.getEmail());
                    oldUser.setAge(user.getAge());

                    return this.userJPARepository.save(oldUser);
                })
                .orElseThrow(() -> new UserNotFoundException(userId));
        return this.userToUserDTOConverter.convert(updatedUser);
    }

    @Transactional
    public void deleteUser(String userId) {
        this.userJPARepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        this.userJPARepository.deleteById(userId);
    }
}
