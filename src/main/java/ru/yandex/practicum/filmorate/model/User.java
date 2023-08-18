package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    @NonNull
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;

    @NonNull
    @NotBlank(message = "Логин не может быть пустым и содержать пробелы")
    private String login;

    private String name;

    @NonNull
    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    @EqualsAndHashCode.Exclude
    private int id;

    public User(@NonNull String email, @NonNull String login, String name, @NonNull LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public String getName() {
        if (name == null || name.isBlank()) {
            name = this.login;
        }
        return name;
    }
}
