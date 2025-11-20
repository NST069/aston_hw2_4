package com.aston.user_service.service;

import com.aston.user_service.exception.UserNotFoundException;
import com.aston.user_service.model.User;
import com.aston.user_service.repository.UserJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserJPARepository userJPARepository;

    @InjectMocks
    UserService userService;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();

        User user1 = new User();
        user1.setName("test1");
        user1.setEmail("test1@mail.ru");
        user1.setAge(30);

        User user2 = new User();
        user2.setName("test2");
        user2.setEmail("test2@mail.ru");
        user2.setAge(41);

        users.add(user1);
        users.add(user2);
    }

    @Test
    void createUser_ShouldCreate() {
        User newUser = new User();
        newUser.setId(1);
        newUser.setName("test");
        newUser.setEmail("test@mail.ru");
        newUser.setAge(30);

        given(userJPARepository.save(newUser)).willReturn(newUser);

        User result = userService.createUser(newUser);

        assertThat(result.getId()).isEqualTo(newUser.getId());
        assertThat(result.getName()).isEqualTo(newUser.getName());
        assertThat(result.getEmail()).isEqualTo(newUser.getEmail());
        assertThat(result.getAge()).isEqualTo(newUser.getAge());

        verify(userJPARepository, times(1)).save(newUser);
    }

    @Test
    void findUserById_ExistingUser_ShouldReturnUser() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@mail.ru");
        user.setAge(30);

        given(userJPARepository.findById("1")).willReturn(Optional.of(user));

        User result = userService.findUserById("1");

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getAge()).isEqualTo(user.getAge());
    }

    @Test
    void findUserById_NonExistingUser_ShouldThrowException() {
        given(userJPARepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findUserById("1");
        });

        verify(userJPARepository, times(1)).findById("1");
    }

    @Test
    void updateUser_ExistingUser_ShouldUpdateUser() {
        User oldUser = new User();
        oldUser.setId(1);
        oldUser.setName("test");
        oldUser.setEmail("test@mail.ru");
        oldUser.setAge(30);

        User update = new User();
        update.setId(oldUser.getId());
        update.setName("test2");
        update.setEmail("test@mail.ru");
        update.setAge(31);

        given(userJPARepository.findById("1")).willReturn(Optional.of(oldUser));
        given(userJPARepository.save(oldUser)).willReturn(oldUser);

        User result = userService.updateUser("1", update);

        assertThat(result.getId()).isEqualTo(update.getId());
        assertThat(result.getName()).isEqualTo(update.getName());
        assertThat(result.getAge()).isEqualTo(update.getAge());

        verify(userJPARepository, times(1)).findById("1");
        verify(userJPARepository, times(1)).save(oldUser);
    }

    @Test
    void updateUser_NonExistingUser_ShouldThrowException() {
        User update = new User();
        update.setName("test2");
        update.setEmail("test@mail.ru");
        update.setAge(31);

        given(userJPARepository.findById("1")).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.updateUser("1", update);
        });

        verify(userJPARepository, times(1)).findById("1");
    }

    @Test
    void deleteUser_ExistingUser_ShouldDeleteUser() {
        User user = new User();
        user.setId(1);
        user.setName("test");
        user.setEmail("test@mail.ru");
        user.setAge(30);

        given(userJPARepository.findById("1")).willReturn(Optional.of(user));
        doNothing().when(userJPARepository).deleteById("1");

        userService.deleteUser("1");

        verify(userJPARepository, times(1)).deleteById("1");
    }

    @Test
    void deleteUser_NonExistingUser_ShouldThrowException() {
        given(userJPARepository.findById("1")).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser("1");
        });

        verify(userJPARepository, times(1)).findById("1");
    }
}
