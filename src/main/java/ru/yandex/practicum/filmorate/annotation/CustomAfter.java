package ru.yandex.practicum.filmorate.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateValidator.class)
@Documented
public @interface CustomAfter {
    String message() default "Incorrect date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
