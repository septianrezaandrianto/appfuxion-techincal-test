package com.techincal.test.appfuxion.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = KeywordValidator.class)
public @interface KeywordValidation {

    String message() default "Invalid field keyword format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
