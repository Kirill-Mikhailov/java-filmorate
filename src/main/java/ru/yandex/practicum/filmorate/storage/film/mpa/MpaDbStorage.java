package ru.yandex.practicum.filmorate.storage.film.mpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT * FROM MPA ORDER BY id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToMpa);
    }

    @Override
    public Mpa getById(Integer id) {
        String sqlQuery = "SELECT * FROM MPA WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Получение рейтинга с неверным id: {}", id);
            throw new MpaNotFoundException(String.format("Рейтинг с id: %d не найден", id));
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(resultSet.getInt("ID"))
                .name(resultSet.getString("MPA_RATING"))
                .build();
    }
}
