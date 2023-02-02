package com.techincal.test.appfuxion.validator;

import com.techincal.test.appfuxion.exception.CustomException;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class RowPerPageValidator implements ConstraintValidator<RowPerPageValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {
        String regexOfDigitOnly = "^[0-9]+$";
        if (!Pattern.compile(regexOfDigitOnly).matcher(String.valueOf(value)).find()) {
            throw CustomException.builder()
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .responseMessage("Invalid field rowPerPage format")
                    .build();
        } else if(Integer.valueOf(value) > 100) {
            throw CustomException.builder()
                    .responseCode(HttpStatus.BAD_REQUEST.value())
                    .responseMessage("You can't input rowPerPage more than 100")
                    .build();
        }
        return true;
    }
}
