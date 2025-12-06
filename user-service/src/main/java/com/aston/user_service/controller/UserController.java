package com.aston.user_service.controller;

import com.aston.user_service.mapper.EmailDTO;
import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.service.NotificationOrchestrator;
import com.aston.user_service.service.UserService;
import com.aston.user_service.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class UserController {

    private final UserService userService;

    private final NotificationOrchestrator notificationOrchestrator;

    @Operation(
            summary = "Добавить пользователя",
            description = "Добавить нового пользователя в базу данных и уведомить его на указанную почту",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь добавлен успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Успешный ответ",
                                            description = "Пример успешного ответа от сервиса",
                                            value = """
                                                    {
                                                        "successful": true,
                                                        "status": 200,
                                                        "message": "Пользователь добавлен успешно",
                                                        "data": {
                                                            "id": 1,
                                                            "name": "Райан Гослинг",
                                                            "email": "ryangosling@bladerunners.net",
                                                            "age": 45
                                                        },
                                                        "_links": {
                                                            "self": {
                                                                "href": "http://localhost:8080/api/v1/users"
                                                            },
                                                            "Get User": {
                                                                "href": "http://localhost:8080/api/v1/users/1"
                                                            }
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Представлены невалидные аргументы",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Ошибка входных данных",
                                            description = "Пример ответа от сервиса, если представлены невалидные данные(одно или несколько полей)",
                                            value = """
                                                    {
                                                        "successful": false,
                                                        "status": 400,
                                                        "message": "Представлены невалидные аргументы",
                                                        "data": {
                                                            "name": "Поле Имя обязательно для заполнения",
                                                            "email": "Некорректный формат email адреса",
                                                            "age": "Возраст должен быть от 1 до 120"
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Найти пользователя",
            description = "Найти пользователя по Id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь найден успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Успешный ответ",
                                            description = "Пример успешного ответа от сервиса",
                                            value = """
                                                    {
                                                        "successful": true,
                                                        "status": 200,
                                                        "message": "Пользователь найден успешно",
                                                        "data": {
                                                            "id": 1,
                                                            "name": "Райан Гослинг",
                                                            "email": "ryangosling@bladerunners.net",
                                                            "age": 45
                                                        },
                                                        "_links": {
                                                            "self": {
                                                                "href": "http://localhost:8080/api/v1/users/1"
                                                            },
                                                            "Update User": {
                                                                "href": "http://localhost:8080/api/v1/users/1"
                                                            },
                                                            "Delete User": {
                                                                "href": "http://localhost:8080/api/v1/users/1"
                                                            }
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Пользователь не найден",
                                            description = "Пример ответа от сервиса при отсутствии пользователя",
                                            value = """
                                                    {
                                                        "successful": false,
                                                        "status": 404,
                                                        "message": "Пользователь с Id 1 не найден"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Обновить пользователя",
            description = "Обновить данные пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные пользователя",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserDTO.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь обновлен успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Успешный ответ",
                                            description = "Пример успешного ответа от сервиса",
                                            value = """
                                                    {
                                                        "successful": true,
                                                        "status": 200,
                                                        "message": "Пользователь обновлен успешно",
                                                        "data": {
                                                            "id": 1,
                                                            "name": "Райан Гослинг",
                                                            "email": "ryangosling@bladerunners.net",
                                                            "age": 45
                                                      }
                                                    },
                                                    "_links": {
                                                        "self": {
                                                            "href": "http://localhost:8080/api/v1/users/1"
                                                        },
                                                        "Get User": {
                                                            "href": "http://localhost:8080/api/v1/users/1"
                                                        },
                                                        "Delete User": {
                                                            "href": "http://localhost:8080/api/v1/users/1"
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Пользователь не найден",
                                            description = "Пример ответа от сервиса при отсутствии пользователя",
                                            value = """
                                                    {
                                                        "successful": false,
                                                        "status": 404,
                                                        "message": "Пользователь с Id 1 не найден"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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

    @Operation(
            summary = "Удалить пользователя",
            description = "Удалить пользователя из базы даных и уведомить его на указанную почту",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь удален успешно",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Успешный ответ",
                                            description = "Пример успешного ответа от сервиса",
                                            value = """
                                                    {
                                                        "successful": true,
                                                        "status": 200,
                                                        "message": "Пользователь удален успешно",
                                                        "data": null,
                                                        "_links": {
                                                            "self": {
                                                                "href": "http://localhost:8080/api/v1/users/1"
                                                            },
                                                            "Add User": {
                                                                "href": "http://localhost:8080/api/v1/users"
                                                            }
                                                        }
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Пользователь не найден",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Result.class),
                                    examples = @ExampleObject(
                                            summary = "Пользователь не найден",
                                            description = "Пример ответа от сервиса при отсутствии пользователя",
                                            value = """
                                                    {
                                                        "successful": false,
                                                        "status": 404,
                                                        "message": "Пользователь с Id 1 не найден"
                                                    }
                                                    """
                                    )
                            )
                    )
            }
    )
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
