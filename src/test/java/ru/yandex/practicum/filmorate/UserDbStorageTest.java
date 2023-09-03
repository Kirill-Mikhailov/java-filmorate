package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserDbStorageTest {

    private final UserStorage userDbStorage;

    @Test
    @Order(1)
    @Sql({"/schema.sql", "/data.sql"})
    void addUserTest() {
        User user = User.builder()
                .login("user1")
                .name("User1 User1")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();

        assertEquals(1, userDbStorage.add(user).getId(), "Пользователи не совпадают");
    }

    @Test
    @Order(2)
    void getUserByIdTest() {
        User user = User.builder()
                .login("user1")
                .name("User1 User1")
                .email("user1@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();

        assertEquals(user, userDbStorage.getById(1), "Пользователи не совпадают");
    }

    @Test
    @Order(3)
    void updateUserTest() {
        User updatedUser = User.builder()
                .login("updatedUser1")
                .name("Updated User1")
                .email("updatedUser1@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .id(1)
                .build();

        assertEquals(updatedUser, userDbStorage.update(updatedUser), "Пользователи не совпадают");
    }

    @Test
    @Order(4)
    void getAllUserTest() {
        User newUser = User.builder()
                .login("user2")
                .name("User2 User2")
                .email("user2@mail.ru")
                .birthday(LocalDate.of(1976, 8, 20))
                .build();

        userDbStorage.add(newUser);

        assertEquals(2, userDbStorage.getAll().size(), "Размер списка не совпадает");
    }

    @Test
    @Order(5)
    void addFriendTest() {
        userDbStorage.addFriend(1, 2);

        assertEquals(1, userDbStorage.getFriends(1).size(), "Размер списка не совпадает");
    }

    @Test
    @Order(6)
    void getFriendsTest() {
        assertEquals(1, userDbStorage.getFriends(1).size(), "Размер списка не совпадает");
    }

    @Test
    @Order(7)
    void getCommonFriendsTest() {
        User newUser = User.builder()
                .login("user3")
                .name("User3 User3")
                .email("user3@mail.ru")
                .birthday(LocalDate.of(1976, 8, 20))
                .build();

        userDbStorage.add(newUser);
        userDbStorage.addFriend(3, 2);

        assertEquals(1, userDbStorage.getCommonFriends(1, 3).size(), "Размер списка не совпадает");
    }

    @Test
    @Order(8)
    void removeFriendTest() {
        userDbStorage.removeFriend(1, 2);
        assertEquals(0, userDbStorage.getFriends(1).size(), "Размер списка не совпадает");
    }
}
