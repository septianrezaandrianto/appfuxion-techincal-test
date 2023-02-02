package com.techincal.test.appfuxion.rest;

import com.techincal.test.appfuxion.request.GithubSearchRequest;
import com.techincal.test.appfuxion.response.GithubSearchDetailResponse;
import com.techincal.test.appfuxion.response.GithubSearchResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GithubSearchRestTest {

    @Mock
    private Logger logger;
    @Mock
    private WebClient webClient;
    @Mock
    private Environment env;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;
    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @InjectMocks
    private GithubSearchRest githubSearchRest;

    String completeUrl = "https://api.github.com/search/users?q={q}&per_page={perPage}"
            .replace("{q}", githubSearchRequest().getQ())
            .replace("{perPage}", String.valueOf(githubSearchRequest().getPer_page()));


    @Test
    @DisplayName("should return doSearchGithubRest_success success")
    public void doSearchGithubRest_success() {
        when(env.getProperty("github.search.url")).thenReturn(completeUrl);

        when(webClient.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<GithubSearchResponse>>notNull()))
                .thenReturn(Mono.just(githubSearchResponse()));

        Mono<GithubSearchResponse> result = githubSearchRest.doSearchGithubRest(githubSearchRequest());
        assertEquals(githubSearchResponse(), result.block());
    }

    private GithubSearchRequest githubSearchRequest() {
        return GithubSearchRequest.builder()
                .q("Q")
                .per_page(1)
                .build();
    }

    private GithubSearchResponse githubSearchResponse() {
        return GithubSearchResponse.builder()
                .total_count(8116)
                .incomplete_results(false)
                .items(githubSearchDetailResponseList())
                .build();
    }

    private List<GithubSearchDetailResponse> githubSearchDetailResponseList() {
        List<GithubSearchDetailResponse> result = new ArrayList<>();
        result.add(githubSearchDetailResponse());
        return result;
    }

    private GithubSearchDetailResponse githubSearchDetailResponse() {
        return GithubSearchDetailResponse.builder()
                .id(65956)
                .login("q")
                .node_id("MDQ6VXNlcjY1OTU2")
                .avatar_url("https://avatars.githubusercontent.com/u/65956?v=4")
                .gravatar_id("")
                .url("https://api.github.com/users/q")
                .html_url("https://github.com/q")
                .followers_url("https://api.github.com/users/q/followers")
                .following_url("https://api.github.com/users/q/following{/other_user}")
                .gists_url("https://api.github.com/users/q/gists{/gist_id}")
                .starred_url("https://api.github.com/users/q/starred{/owner}{/repo}")
                .subscriptions_url("https://api.github.com/users/q/subscriptions")
                .organizations_url("https://api.github.com/users/q/orgs")
                .repos_url("https://api.github.com/users/q/repos")
                .events_url("https://api.github.com/users/q/events{/privacy}")
                .received_events_url("https://api.github.com/users/q/received_events")
                .type("User")
                .site_admin(false)
                .score(1.0)
                .build();
    }
}
