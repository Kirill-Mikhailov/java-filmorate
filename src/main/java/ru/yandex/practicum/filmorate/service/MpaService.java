package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaStorage mpaDbStorage;

    public List<Mpa> getAll() {
        return mpaDbStorage.getAll();
    }

    public Mpa getById(Integer id) {
        return mpaDbStorage.getById(id);
    }
}
