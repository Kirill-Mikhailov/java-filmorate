package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {

    private final GenreStorage genreDbStorage;

    @Test
    void getAllGenresTest() {
        assertEquals(6, genreDbStorage.getAll().size(), "Размер списка не совпадает");
    }

    @Test
    void getGenreByIdTest() {
        Genre genre = Genre.builder().id(1).name("Комедия").build();

        assertEquals(genre, genreDbStorage.getById(1), "Жанры не совпадают");
    }
}
