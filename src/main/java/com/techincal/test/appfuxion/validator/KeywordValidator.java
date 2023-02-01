package com.techincal.test.appfuxion.validator;

import com.techincal.test.appfuxion.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class KeywordValidator implements ConstraintValidator<KeywordValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        if(Objects.isNull(value) || ObjectUtils.isEmpty(value.trim())) {
            throw CustomException.builder()
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .responseMessage("Invalid field keyword format")
                    .build();
        }

        return true;
    }
}
