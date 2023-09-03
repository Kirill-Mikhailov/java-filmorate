package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.film.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaDbStorage;
    private final GenreStorage genreDbStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MpaStorage mpaDbStorage, GenreStorage genreDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "SELECT * FROM FILMS WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Получение фильма с неверным id: {}", id);
            throw new FilmNotFoundException("Фильм с id " + id + " не существует");
        }
    }

    @Override
    public Film add(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("ID");
        int id = simpleJdbcInsert.executeAndReturnKey(toMap(film)).intValue();
        addFilmsGenres(film, id);
        return getById(id);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?, MPA_ID = ? WHERE ID = ?";
        try {
            getById(film.getId());
        } catch (FilmNotFoundException e) {
            log.debug("Обновление фильма c неверным id: {}", film.getId());
            throw new FilmNotFoundException("Фильм с id " + film.getId() + " не существует");
        }
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        addFilmsGenres(film, film.getId());
        return getById(film.getId());
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        String sqlQuery = "INSERT INTO LIKES(FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public void removeLike(Integer id, Integer userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    @Override
    public List<Film> getPopular(Integer count) {
        String sqlQuery = "SELECT f.* FROM FILMS AS f LEFT JOIN LIKES AS l ON f.ID = l.FILM_ID " +
                "GROUP BY f.ID ORDER BY COUNT(l.FILM_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private void addFilmsGenres(Film film, Integer id) {
        if (!genreDbStorage.getByFilmId(film.getId()).isEmpty()) {
            String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
            jdbcTemplate.update(sqlQuery, film.getId());
        }
        if (film.getGenres() == null) {
            return;
        }
         for (Genre g: film.getGenres()) {
            String sqlQuery = "INSERT INTO FILM_GENRES(FILM_ID, GENRE_ID) " +
                    "VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, id, g.getId());
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("NAME"))
                .description(resultSet.getString("DESCRIPTION"))
                .releaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate())
                .duration(resultSet.getInt("DURATION"))
                .mpa(mpaDbStorage.getById(resultSet.getInt("MPA_ID")))
                .genres(genreDbStorage.getByFilmId(resultSet.getInt("ID")))
                .build();
    }

    public Map<String, Object> toMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("NAME", film.getName());
        values.put("DESCRIPTION", film.getDescription());
        values.put("RELEASE_DATE", film.getReleaseDate());
        values.put("DURATION", film.getDuration());
        values.put("MPA_ID", film.getMpa().getId());
        return values;
    }
}
