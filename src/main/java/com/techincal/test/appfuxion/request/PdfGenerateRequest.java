package com.techincal.test.appfuxion.request;

import com.techincal.test.appfuxion.validator.KeywordValidation;
import com.techincal.test.appfuxion.validator.RowPerPageValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PdfGenerateRequest {

    @KeywordValidation
    private String keyword;
    @RowPerPageValidation
    private String rowPerPage;

}
