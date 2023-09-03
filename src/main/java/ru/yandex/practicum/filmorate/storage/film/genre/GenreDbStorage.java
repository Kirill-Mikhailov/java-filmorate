package ru.yandex.practicum.filmorate.storage.film.genre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM GENRE ORDER BY ID";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getById(Integer id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Получение жанра с неверным id: {}", id);
            throw new GenreNotFoundException(String.format("Жанр с id: %d не найден", id));
        }
    }

    @Override
    public List<Genre> getByFilmId(Integer id) {
        String sqlQuery = "SELECT * FROM GENRE WHERE ID IN (SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?)";
        return jdbcTemplate.query(sqlQuery, this::mapRowToGenre, id);
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("GENRE_NAME"))
                .build();
    }
}
