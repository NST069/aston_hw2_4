package com.aston.user_service.controller;

import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.service.UserService;
import com.aston.user_service.util.Result;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Result addUser(@Valid @RequestBody UserDTO userDTO) {
        logger.debug("Создание пользователя: {}", userDTO);
        UserDTO savedUserDTO = this.userService.createUser(userDTO);
        logger.debug("Пользователь добавлен успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь добавлен успешно", savedUserDTO);
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable String userId) {
        logger.debug("Поиск пользователя по Id {}", userId);
        UserDTO foundUserDTO = this.userService.findUserById(userId);
        logger.debug("Пользователь найден успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь найден успешно", foundUserDTO);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        logger.debug("Обновление пользователя: id={}", userId);
        UserDTO updatedUserDTO = this.userService.updateUser(userId, userDTO);
        logger.debug("Пользователь обновлен успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь обновлен успешно", updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable String userId) {
        logger.debug("Удаление пользователя: id={}", userId);
        this.userService.deleteUser(userId);
        logger.debug("Пользователь удален успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь удален успешно", null);
    }

}
