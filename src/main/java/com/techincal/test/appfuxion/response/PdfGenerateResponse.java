package com.techincal.test.appfuxion.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfGenerateResponse {

    private Integer responseCode;
    private String responseMessage;
    private String fileName;
    private String generateTime;

}
