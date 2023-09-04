package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = "SELECT * FROM USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User getById(Integer id) {
        String sqlQuery = "SELECT * FROM USERS WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Получение пользователя с неверным id: {}", id);
            throw new UserNotFoundException("Пользователь с id " + id + " не существует");
        }
    }

    @Override
    public User add(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("USERS")
                .usingGeneratedKeyColumns("ID");
        int id = simpleJdbcInsert.executeAndReturnKey(toMap(user)).intValue();
        return getById(id);
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE ID = ?";
        try {
            getById(user.getId());
        } catch (UserNotFoundException e) {
            log.debug("Обновление пользователя c неверным id: {}", user.getId());
            throw new UserNotFoundException("Пользователь с id " + user.getId() + " не существует");
        }
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return getById(user.getId());
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void removeFriend(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        String sqlQuery = "SELECT * FROM USERS WHERE ID IN (SELECT FRIEND_ID FROM FRIENDSHIP WHERE USER_ID = ?);";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT * FROM USERS WHERE ID IN " +
                "(SELECT f.FRIEND_ID FROM FRIENDSHIP f WHERE f.USER_ID = ? AND f.FRIEND_ID IN " +
                "(SELECT f.FRIEND_ID FROM FRIENDSHIP f WHERE f.USER_ID = ?))";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getInt("ID"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .name(resultSet.getString("NAME"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    public Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("EMAIL", user.getEmail());
        values.put("LOGIN", user.getLogin());
        values.put("NAME", user.getName());
        values.put("BIRTHDAY", user.getBirthday());
        return values;
    }
}
