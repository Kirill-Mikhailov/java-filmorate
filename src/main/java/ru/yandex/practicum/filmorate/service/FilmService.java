package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmDbStorage;

    public List<Film> getAllFilms() {
        return filmDbStorage.getAll();
    }

    public Film addFilm(Film film) {
        return filmDbStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return filmDbStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        return  filmDbStorage.getById(id);
    }

    public void addLike(Integer id, Integer userId) {
        getFilmById(id);
        filmDbStorage.addLike(id, userId);
    }

    public void removeLike(Integer id, Integer userId) {
        getFilmById(id);
        filmDbStorage.removeLike(id, userId);
    }

    public List<Film> getPopular(Integer count) {
        return filmDbStorage.getPopular(count);
    }
}
