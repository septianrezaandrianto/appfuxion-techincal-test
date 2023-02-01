package com.techincal.test.appfuxion.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotFoundException extends  RuntimeException {

    private Integer responseCode;
    private String responseMessage;

    public NotFoundException(String message) {
        super(message);
    }
}
