package com.aston.user_service.controller;

import com.aston.user_service.mapper.UserDTO;
import com.aston.user_service.util.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserAPI {

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
    Result addUser(@Valid @RequestBody UserDTO userDTO);

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
    Result getUserById(@PathVariable String userId);

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
                                                        "data": %s
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
    Result updateUser(@PathVariable String userId, @Valid @RequestBody UserDTO userDTO);

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
    Result deleteUser(@PathVariable String userId);
}
