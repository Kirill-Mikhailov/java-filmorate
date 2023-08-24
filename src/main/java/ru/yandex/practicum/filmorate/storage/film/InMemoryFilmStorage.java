package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private int id;
    private final HashMap<Integer, Film> films = new HashMap<>();

    private int getNewId() {
        return ++id;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film add(Film film) {
        film.setId(getNewId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.debug("Обновление фильма c неверным id: {}", film);
            throw new FilmNotFoundException("Фильма с id " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getById(Integer id) {
        if (!films.containsKey(id)) {
            log.debug("Получение фильма с неверным id: {}", id);
            throw new FilmNotFoundException("Фильма с id " + id + " не существует");
        }
        return films.get(id);
    }
}
