package ru.yandex.practicum.filmorate.storage.film.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    List<Genre> getAll();

    Genre getById(Integer id);

    List<Genre> getByFilmId(Integer id);
}
