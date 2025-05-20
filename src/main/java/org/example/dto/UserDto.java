package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Schema(description = "DTO пользователя, содержащий основные данные пользователя")
@RequiredArgsConstructor
@Getter
@Setter
public class UserDto {

    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "Иван")
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @Schema(description = "Email пользователя", example = "ivan@example.com")
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат Email")
    private String email;

    @Schema(description = "Возраст пользователя", example = "25", minimum = "1")
    @NotNull(message = "Возраст обязателен")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    private Integer age;
}