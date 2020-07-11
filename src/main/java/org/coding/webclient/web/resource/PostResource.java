package org.coding.webclient.web.resource;


import org.coding.webclient.dto.Post;
import org.coding.webclient.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/posts")
public class PostResource {
    @Autowired
    private PostService service;

    @PostMapping
    public Mono<Post> add(@RequestBody  Post post){
        return service.add(post);
    }
}
