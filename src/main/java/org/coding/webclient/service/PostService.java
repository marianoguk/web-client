package org.coding.webclient.service;

import org.coding.webclient.dto.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Service
public class PostService {

    @Autowired
    private WebClient webClient;

    public Mono<Post> add(Post newPost) {
        return webClient.post()
                .body(Mono.just(newPost), Post.class)
                .retrieve()
                .bodyToMono(Post.class);
    }

}
