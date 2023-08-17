package ru.yandex.practicum.filmorate;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.time.LocalDate;

public class FilmValidationTest {
    Film film;
    FilmValidation filmValidation = new FilmValidation();

    @BeforeEach
    void beforeEach() {
        film = new Film("Название", "Описание", LocalDate.of(2011, 11, 11),
                120);
    }

    @Test
    void emptyNameFilm() {
        film.setName("");
        ValidationException emptyName = assertThrows(
                ValidationException.class,
                () -> filmValidation.validate(film)
        );
        assertEquals("Название фильма не может быть пустым", emptyName.getMessage());
    }

    @Test
    void descriptionWith201CharactersFilm() {
        film.setDescription("Это описание имеет ровно 201 символ. Для этого пришлось написать много лишних " +
                "слов, ведь набрать точное количество символов не так-то просто, как кажется на первый " +
                "взгляд. Но главное сильно постараться");
        ValidationException longDescription = assertThrows(
                ValidationException.class,
                () -> filmValidation.validate(film)
        );
        assertEquals("Максимальная длина описания — 200 символов", longDescription.getMessage());
    }

    @Test
    void earlyReleaseDateFilm() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException earlyReleaseDate = assertThrows(
                ValidationException.class,
                () -> filmValidation.validate(film)
        );
        assertEquals("Дата релиза не может быть раньше 28 декабря 1895 года", earlyReleaseDate.getMessage());
    }

    @Test
    void zeroDurationFilm() {
        film.setDuration(0);
        ValidationException zeroDuration = assertThrows(
                ValidationException.class,
                () -> filmValidation.validate(film)
        );
        assertEquals("Продолжительность фильма должна быть положительной", zeroDuration.getMessage());

    }

    @Test
    void NegativeDurationFilm() {
        film.setDuration(-1);
        ValidationException negativeDuration = assertThrows(
                ValidationException.class,
                () -> filmValidation.validate(film)
        );
        assertEquals("Продолжительность фильма должна быть положительной", negativeDuration.getMessage());
    }
}
