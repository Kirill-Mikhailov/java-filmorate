package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage inMemoryFilmStorage;

    public List<Film> getAllFilms() {
        return inMemoryFilmStorage.getAll();
    }

    public Film addFilm(Film film) {
        return inMemoryFilmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryFilmStorage.update(film);
    }

    public Film getFilmById(Integer id) {
        return  inMemoryFilmStorage.getById(id);
    }

    public void addLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        film.getLikes().add(userId);
    }

    public void removeLike(Integer id, Integer userId) {
        Film film = getFilmById(id);
        film.getLikes().remove(userId);
    }

    public List<Film> getPopular(Integer count) {
        return getAllFilms().stream().sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}
