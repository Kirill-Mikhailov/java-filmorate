package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MpaStorage mpaDbStorage;

    @Test
    void getAllGenresTest() {
        assertEquals(5, mpaDbStorage.getAll().size(), "Размер списка не совпадает");
    }

    @Test
    void getGenreByIdTest() {
        Mpa mpa = Mpa.builder().id(1).name("G").build();

        assertEquals(mpa, mpaDbStorage.getById(1), "Рейтинги не совпадают");
    }
}
