package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.annotation.CustomAfter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Film {

    @NonNull
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;

    @NonNull
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    private String description;

    @NonNull
    @Past(message = "Дата релиза не может быть в будущем")
    @CustomAfter(message = "Дата релиза не может быть раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @NonNull
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private int duration;

    @JsonIgnore
    private final Set<Integer> likes = new TreeSet<>();

    @EqualsAndHashCode.Exclude
    private int id;
}
