package com.techincal.test.appfuxion.rest;

import com.techincal.test.appfuxion.request.GithubSearchRequest;
import com.techincal.test.appfuxion.response.GithubSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class GithubSearchRest {

    Logger logger = LoggerFactory.getLogger(GithubSearchRest.class);

    @Value("${github.search.url}")
    private String githubSearchUrl;
    @Autowired
    private WebClient webClient;
    @Autowired
    private Environment env;

    public Mono<GithubSearchResponse> doSearchGithubRest(GithubSearchRequest githubSearchRequest) {
        String completeUrl = env.getProperty("github.search.url").replace("{q}", githubSearchRequest.getQ())
                .replace("{perPage}", String.valueOf(githubSearchRequest.getPer_page()));

        logger.info("URL " + completeUrl);
        return webClient.get()
                .uri(completeUrl)
                .retrieve().bodyToMono(GithubSearchResponse.class);
    }
}
