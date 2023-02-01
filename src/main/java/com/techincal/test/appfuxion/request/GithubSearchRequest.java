package com.techincal.test.appfuxion.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GithubSearchRequest {

    private String q;
    private Integer per_page;

}
