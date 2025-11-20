package com.aston.user_service.controller;

import com.aston.user_service.exception.UserNotFoundException;
import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.model.User;
import com.aston.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void addUser_ShouldSave() throws Exception {
        UserDTO userDTO = new UserDTO(1, "test", "test@mail.ru", 30);
        String json = objectMapper.writeValueAsString(userDTO);

        User savedUser = new User();
        savedUser.setId(1);
        savedUser.setName("test");
        savedUser.setEmail("test@mail.ru");
        savedUser.setAge(30);

        given(this.userService.createUser(Mockito.any(User.class))).willReturn(savedUser);

        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Пользователь добавлен успешно"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("test"))
                .andExpect(jsonPath("$.data.email").value("test@mail.ru"))
                .andExpect(jsonPath("$.data.age").value(30))
        ;
    }

    @Test
    void addUser_InvalidEmail_ShouldThrowException() throws Exception {
        UserDTO userDTO = new UserDTO(1, "test", "invalidEmail", 30);
        String json = objectMapper.writeValueAsString(userDTO);

        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value("Представлены невалидные аргументы"))
                .andExpect(jsonPath("$.data.email").value("Некорректный формат email адреса"))
        ;
    }

    @Test
    void getUserById_ExistingUser_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setId(1);
        user.setName("test1");
        user.setEmail("test1@mail.ru");
        user.setAge(30);

        given(this.userService.findUserById("1")).willReturn(user);

        this.mockMvc.perform(get("/api/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Пользователь найден успешно"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.email").value("test1@mail.ru"))
        ;
    }

    @Test
    void getUserById_NonExistingUser_ShouldThrowException() throws Exception {
        given(this.userService.findUserById("1")).willThrow(new UserNotFoundException("1"));

        this.mockMvc.perform(get("/api/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с Id 1 не найден"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    void updateUser_ExistingUser_ShouldUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("test2");
        updatedUser.setEmail("test2@mail.ru");
        updatedUser.setAge(20);

        UserDTO userDTO = new UserDTO(1, "test", "test@mail.ru", 30);
        String json = objectMapper.writeValueAsString(userDTO);

        given(this.userService.updateUser(eq("1"), Mockito.any(User.class))).willReturn(updatedUser);

        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Пользователь обновлен успешно"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("test2"))
                .andExpect(jsonPath("$.data.email").value("test2@mail.ru"))
                .andExpect(jsonPath("$.data.age").value(20))
        ;
    }

    @Test
    void updateUser_NonExistingUser_ShouldThrowException() throws Exception {
        UserDTO userDTO = new UserDTO(1, "test", "test@mail.ru", 30);
        String json = objectMapper.writeValueAsString(userDTO);

        given(this.userService.updateUser(eq("1"), Mockito.any(User.class))).willThrow(new UserNotFoundException("1"));

        this.mockMvc.perform(put("/api/v1/users/1")
                        .contentType(MediaType.APPLICATION_JSON).content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с Id 1 не найден"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    void deleteUser_ExistingUser_ShouldUpdateUser() throws Exception {
        doNothing().when(this.userService).deleteUser("1");

        this.mockMvc.perform(delete("/api/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(true))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Пользователь удален успешно"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }

    @Test
    void deleteUser_NonExistingUser_ShouldThrowException() throws Exception {
        doThrow(new UserNotFoundException("1")).when(this.userService).deleteUser("1");

        this.mockMvc.perform(delete("/api/v1/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.successful").value(false))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Пользователь с Id 1 не найден"))
                .andExpect(jsonPath("$.data").isEmpty())
        ;
    }
}
