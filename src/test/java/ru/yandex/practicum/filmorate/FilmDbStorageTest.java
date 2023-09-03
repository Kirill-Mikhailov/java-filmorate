package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilmDbStorageTest {

    private final FilmStorage filmDbStorage;
    private final UserStorage userDbStorage;

    @Test
    @Order(1)
    @Sql({"/schema.sql", "/data.sql"})
    void addFilmTest() {
        Film film = Film.builder()
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.of(1970, 5, 17))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        assertEquals(1, filmDbStorage.add(film).getId(), "Id фильмов не совпадают");
    }

    @Test
    @Order(2)
    void getFilmByIdTest() {
        Film film = Film.builder()
                .id(1)
                .name("film1")
                .description("description1")
                .releaseDate(LocalDate.of(1970, 5, 17))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        assertEquals(film, filmDbStorage.getById(1), "Фильмы не совпадают");
    }

    @Test
    @Order(3)
    void updateFilmTest() {
        Film updatedFilm = Film.builder()
                .id(1)
                .name("updatedFilm1")
                .description("updatedDescription1")
                .releaseDate(LocalDate.of(1970, 5, 17))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        assertEquals(updatedFilm, filmDbStorage.update(updatedFilm), "Фильмы не совпадают");
    }

    @Test
    @Order(4)
    void getAllFilmsTest() {
        Film newFilm = Film.builder()
                .name("film2")
                .description("description2")
                .releaseDate(LocalDate.of(1970, 5, 17))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        filmDbStorage.add(newFilm);

        assertEquals(2, filmDbStorage.getAll().size(), "Размер списка не совпадает");
    }

    @Test
    @Order(5)
    void getPopular() {
        Film updatedFilm = Film.builder()
                .id(1)
                .name("updatedFilm1")
                .description("updatedDescription1")
                .releaseDate(LocalDate.of(1970, 5, 17))
                .duration(120)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();

        User user = User.builder()
                .login("user1")
                .name("User1 User1")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        userDbStorage.add(user);
        filmDbStorage.addLike(1, 1);

        assertEquals(updatedFilm, filmDbStorage.getPopular(10).get(0), "Фильмы не совпадают");
    }
}
