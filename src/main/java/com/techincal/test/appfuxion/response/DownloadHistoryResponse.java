package com.techincal.test.appfuxion.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.techincal.test.appfuxion.entity.DownloadHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DownloadHistoryResponse {

    private Integer responseCode;
    private String responseMessage;
    private Integer totalData;
    private List<DownloadHistory> dataList;

}
