package com.aston.user_service.controller;

import com.aston.user_service.mapper.EmailDTO;
import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.service.NotificationOrchestrator;
import com.aston.user_service.service.UserService;
import com.aston.user_service.util.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final NotificationOrchestrator notificationOrchestrator;

    @PostMapping
    public Result addUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("Создание пользователя: {}", userDTO);
        UserDTO savedUserDTO = this.userService.createUser(userDTO);
        log.debug("Пользователь добавлен успешно");
        notificationOrchestrator.sendNotification(new EmailDTO(userDTO.email(), "create", "Уведомление user-service"));
        return new Result(true, HttpStatus.OK.value(), "Пользователь добавлен успешно", savedUserDTO);
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable String userId) {
        log.debug("Поиск пользователя по Id {}", userId);
        UserDTO foundUserDTO = this.userService.findUserById(userId);
        log.debug("Пользователь найден успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь найден успешно", foundUserDTO);
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        log.debug("Обновление пользователя: id={}", userId);
        UserDTO updatedUserDTO = this.userService.updateUser(userId, userDTO);
        log.debug("Пользователь обновлен успешно");
        return new Result(true, HttpStatus.OK.value(), "Пользователь обновлен успешно", updatedUserDTO);
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable String userId) {
        log.debug("Удаление пользователя: id={}", userId);
        UserDTO userDTO = this.userService.findUserById(userId);
        this.userService.deleteUser(userId);
        log.debug("Пользователь удален успешно");
        notificationOrchestrator.sendNotification(new EmailDTO(userDTO.email(), "delete", "Уведомление user-service"));
        return new Result(true, HttpStatus.OK.value(), "Пользователь удален успешно", null);
    }

}
