package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {
    User user;
    UserValidation userValidation = new UserValidation();

    @BeforeEach
    void beforeEach() {
        user = new User("email@yandex.ru", "qwerty", "Name",
                LocalDate.of(1999, 11, 17));
    }

    @Test
    void emptyEmailUser() {
        user.setEmail("");
        ValidationException emptyEmail = assertThrows(
                ValidationException.class,
                () -> userValidation.validate(user)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                emptyEmail.getMessage());
    }

    @Test
    void incorrectEmailUser() {
        user.setEmail("email");
        ValidationException incorrectEmail = assertThrows(
                ValidationException.class,
                () -> userValidation.validate(user)
        );
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @",
                incorrectEmail.getMessage());
    }

    @Test
    void emptyLoginUser() {
        user.setLogin("");
        ValidationException emptyLogin = assertThrows(
                ValidationException.class,
                () -> userValidation.validate(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", emptyLogin.getMessage());
    }

    @Test
    void loginWithSpacesUser() {
        user.setLogin("incorrect login");
        ValidationException incorrectLogin = assertThrows(
                ValidationException.class,
                () -> userValidation.validate(user)
        );
        assertEquals("Логин не может быть пустым и содержать пробелы", incorrectLogin.getMessage());
    }

    @Test
    void emptyNameUser() {
        user.setName("");
        userValidation.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void nullableNameUser() {
        user.setName(null);
        userValidation.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void incorrectBirthdayUser() {
        user.setBirthday(LocalDate.now().plusDays(1));
        ValidationException incorrectBirthday = assertThrows(
                ValidationException.class,
                () -> userValidation.validate(user)
        );
        assertEquals("Дата рождения не может быть в будущем", incorrectBirthday.getMessage());
    }
}
