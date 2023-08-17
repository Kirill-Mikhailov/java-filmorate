package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final FilmValidation filmValidation = new FilmValidation();

    private int getNewId() {
        return this.id++;
    }

    @GetMapping
    public List<Film> allFilms() {
        log.info("Получение списка всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        filmValidation.validate(film);
        film.setId(getNewId());
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        filmValidation.validate(film);
        if (!films.containsKey(film.getId())) {
            log.debug("Обновление фильма c неверным id: {}", film);
            throw new ValidationException("Фильма с id " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        return film;
    }
}
