package com.techincal.test.appfuxion.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubSearchResponse {

    private Integer total_count;
    private Boolean incomplete_results;
    private List<GithubSearchDetailResponse> items;

}
