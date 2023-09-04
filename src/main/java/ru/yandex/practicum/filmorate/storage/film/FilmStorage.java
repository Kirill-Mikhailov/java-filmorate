package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film update(Film film);

    Film add(Film film);

    Film getById(Integer id);

    void addLike(Integer id, Integer userId);

    void removeLike(Integer id, Integer userId);

    List<Film> getPopular(Integer count);
}
