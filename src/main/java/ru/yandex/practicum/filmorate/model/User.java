package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
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

    @JsonIgnore
    private final Set<Integer> friends = new TreeSet<>();

    @EqualsAndHashCode.Exclude
    private Integer id;

    public String getName() {
        if (name == null || name.isBlank()) {
            name = this.login;
        }
        return name;
    }
}
