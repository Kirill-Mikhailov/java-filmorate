package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@Builder
public class User {

    @NonNull
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NonNull
    @Pattern(regexp = "^\\S+$", message = "Логин не может быть пустым и содержать пробелы")
    private String login;

    private String name;

    @NonNull
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private Integer id;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = this.login;
        }
        return name;
    }
}
