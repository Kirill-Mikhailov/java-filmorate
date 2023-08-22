package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private int id = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();

    private int getNewId() {
        return this.id++;
    }

    @GetMapping
    public List<Film> allFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        film.setId(getNewId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        if (!films.containsKey(film.getId())) {
            log.debug("Обновление фильма c неверным id: {}", film);
            throw new ValidationException("Фильма с id " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        return film;
    }
}
