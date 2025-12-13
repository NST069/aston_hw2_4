package com.aston.user_service.controller;

import com.aston.user_service.mapper.EmailDTO;
import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.service.NotificationOrchestrator;
import com.aston.user_service.service.UserService;
import com.aston.user_service.util.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Управление пользователями")
public class UserController implements UserAPI{

    private final UserService userService;

    private final NotificationOrchestrator notificationOrchestrator;

    @PostMapping
    public Result addUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("Создание пользователя: {}", userDTO);
        UserDTO savedUserDTO = this.userService.createUser(userDTO);
        log.debug("Пользователь добавлен успешно");
        notificationOrchestrator.sendNotification(new EmailDTO(userDTO.email(), "create", "Уведомление user-service"));
        Result result = new Result(true, HttpStatus.OK.value(), "Пользователь добавлен успешно", savedUserDTO);
        result.add(linkTo(methodOn(UserController.class).addUser(userDTO)).withSelfRel());
        result.add(linkTo(methodOn(UserController.class).getUserById(""+savedUserDTO.id())).withRel("Get User"));
        return result;
    }

    @GetMapping("/{userId}")
    public Result getUserById(@PathVariable String userId) {
        log.debug("Поиск пользователя по Id {}", userId);
        UserDTO foundUserDTO = this.userService.findUserById(userId);
        log.debug("Пользователь найден успешно");
        Result result = new Result(true, HttpStatus.OK.value(), "Пользователь найден успешно", foundUserDTO);
        result.add(linkTo(methodOn(UserController.class).getUserById(userId)).withSelfRel());
        result.add(linkTo(methodOn(UserController.class).updateUser(userId, foundUserDTO)).withRel("Update User"));
        result.add(linkTo(methodOn(UserController.class).deleteUser(userId)).withRel("Delete User"));
        return result;
    }

    @PutMapping("/{userId}")
    public Result updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO) {
        log.debug("Обновление пользователя: id={}", userId);
        UserDTO updatedUserDTO = this.userService.updateUser(userId, userDTO);
        log.debug("Пользователь обновлен успешно");
        Result result = new Result(true, HttpStatus.OK.value(), "Пользователь обновлен успешно", updatedUserDTO);
        result.add(linkTo(methodOn(UserController.class).updateUser(userId, userDTO)).withSelfRel());
        result.add(linkTo(methodOn(UserController.class).getUserById(userId)).withRel("Get User"));
        result.add(linkTo(methodOn(UserController.class).deleteUser(userId)).withRel("Delete User"));
        return result;
    }

    @DeleteMapping("/{userId}")
    public Result deleteUser(@PathVariable String userId) {
        log.debug("Удаление пользователя: id={}", userId);
        UserDTO userDTO = this.userService.findUserById(userId);
        this.userService.deleteUser(userId);
        log.debug("Пользователь удален успешно");
        notificationOrchestrator.sendNotification(new EmailDTO(userDTO.email(), "delete", "Уведомление user-service"));
        Result result = new Result(true, HttpStatus.OK.value(), "Пользователь удален успешно", null);
        result.add(linkTo(methodOn(UserController.class).deleteUser(userId)).withSelfRel());
        result.add(linkTo(methodOn(UserController.class).addUser(userDTO)).withRel("Add User"));
        return result;
    }

}
