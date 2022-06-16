package com.webflux.webflux.controller;

import java.net.URI;

import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ApiItemController {

    private final ItemRepository repository;

    @GetMapping("/api/items")
    public Flux<Item> findAll() {
        return this.repository.findAll();
    }

    @GetMapping("/api/items/{id}")
    public Mono<Item> findOne(@PathVariable String id) {
        return this.repository.findById(id);
    }

    @PostMapping("/api/items")
    public Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<Item> item) {
        return item.flatMap(repository::save)
                .map(savedItem -> ResponseEntity
                        .created(URI.create("/api/items/" + savedItem.getId()))
                        .body(savedItem));
    }

    @PutMapping("/api/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<Item> item, @PathVariable String id) {
        return item
                .map(content -> new Item(id, content.getName(), content.getDescription(), content.getPrice()))
                .flatMap(repository::save)
                .map(ResponseEntity::ok);
    }
}