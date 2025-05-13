package org.example.dto;

 import jakarta.validation.constraints.Email;
 import jakarta.validation.constraints.NotBlank;
 import jakarta.validation.constraints.NotNull;
 import jakarta.validation.constraints.Min;

 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @NotBlank(message = "Email ообязателен")
    @Email(message = "Некорректный формат Email")
    private String email;

    @NotNull(message = "Возраст обязателен")
    @Min(value = 1, message = "Возраст должен быть больше 0")
    private Integer age;
}
