package org.antop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antop.Headers;
import org.antop.model.User;
import org.antop.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ApiController {
    private final UserRepository repository;

    @PostMapping("/echo")
    public String echo(@RequestBody String input) {
        log.info("echo : {}", input);
        return input;
    }

    @PostMapping("/user")
    public ResponseEntity<User> registerUser(@RequestBody RegisterUser request, UriComponentsBuilder builder) {
        log.info("register user : name = {}, address = {}", request.getName(), request.getAddress());
        User user = repository.save(request.getName(), request.getAddress());
        log.info("created user : id = {}", user.getId());
        URI uri = builder.replacePath("/user/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable("userId") long userId) {
        return repository.get(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/check")
    public void apply(@RequestHeader(Headers.X_USER_ID) long userId) {
        log.info("check : userId = {}", userId);
    }

}
