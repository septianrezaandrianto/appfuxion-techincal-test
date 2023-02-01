package com.techincal.test.appfuxion.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomException extends  RuntimeException {

    private Integer responseCode;
    private String responseMessage;

    public CustomException(String message) {
        super(message);
    }
}
