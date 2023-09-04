package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreDbStorage;

    public List<Genre> getAll() {
        return genreDbStorage.getAll();
    }

    public Genre getById(Integer id) {
        return genreDbStorage.getById(id);
    }
}
