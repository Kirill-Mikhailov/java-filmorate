package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получение списка всех фильмов");
        return filmService.getAllFilms();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавление фильма: {}", film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@Valid @Positive @PathVariable Integer id) {
        log.info("Получение фильма по id: {}", id);
        return filmService.getFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@Valid @Positive @PathVariable Integer id,
                        @Valid @Positive @PathVariable Integer userId) {
        log.info("Добавление лайка фильму id: {} от пользователя userId: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@Valid @Positive @PathVariable Integer id,
                           @Valid @Positive @PathVariable Integer userId) {
        log.info("Удаление лайка у фильма id: {} от пользователя userId: {}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@Valid @Positive @RequestParam(defaultValue = "10", required = false) Integer count) {
        log.info("список из первых count: {} фильмов по количеству лайков", count);
        return filmService.getPopular(count);
    }
}
