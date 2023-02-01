package com.techincal.test.appfuxion.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = RowPerPageValidator.class)
public @interface RowPerPageValidation {

    String message() default "Invalid field rowPerPage format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
