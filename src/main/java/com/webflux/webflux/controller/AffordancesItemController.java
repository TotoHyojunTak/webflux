package com.webflux.webflux.controller;


import static org.springframework.hateoas.mediatype.alps.Alps.*;
import static org.springframework.hateoas.server.reactive.WebFluxLinkBuilder.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import reactor.core.publisher.Mono;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.mediatype.alps.Alps;
import org.springframework.hateoas.mediatype.alps.Type;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Greg Turnquist
 */
// tag::intro[]
@RequiredArgsConstructor
@RestController
public class AffordancesItemController {
    private final ItemRepository repository;

    @PutMapping("/affordances/items/{id}")
    public Mono<ResponseEntity<?>> updateItem(@RequestBody Mono<EntityModel<Item>> item,
                                              @PathVariable String id) {
        return item
                .map(EntityModel::getContent)
                .map(content -> new Item(id, content.getName(), content.getDescription(), content.getPrice()))
                .flatMap(repository::save)
                .then(findOne(id))
                .map(model -> ResponseEntity.noContent()
                        .location(model.getRequiredLink(IanaLinkRelations.SELF).toUri()).build());
    }

    @GetMapping("/affordances/items/{id}") // <1>
    Mono<EntityModel<Item>> findOne(@PathVariable String id) {
        AffordancesItemController controller = methodOn(AffordancesItemController.class); // <2>

        Mono<Link> selfLink = linkTo(controller.findOne(id)) //
                .withSelfRel() //
                .andAffordance(controller.updateItem(null, id)) // <3>
                .toMono();

        Mono<Link> aggregateLink = linkTo(controller.findAll()) //
                .withRel(IanaLinkRelations.ITEM) //
                .toMono();

        return Mono.zip(repository.findById(id), selfLink, aggregateLink) //
                .map(o -> EntityModel.of(o.getT1(), Links.of(o.getT2(), o.getT3())));
    }

    @GetMapping("/affordances/items")
    Mono<CollectionModel<EntityModel<Item>>> findAll() {
        AffordancesItemController controller = methodOn(AffordancesItemController.class);

        Mono<Link> aggragateRoot = linkTo(controller.findAll())
                .withSelfRel()
                .andAffordance(controller.addNewItem(null))
                .toMono();

        return repository.findAll()
                .flatMap(item -> findOne(item.getId()))
                .collectList()
                .flatMap(models -> aggragateRoot
                        .map(selfLink -> CollectionModel.of(
                                models, selfLink)));
    }

    @PostMapping("/affordances/items") // <1>
    Mono<ResponseEntity<?>> addNewItem(@RequestBody Mono<EntityModel<Item>> item) {
        return item
                .map(EntityModel::getContent)
                .flatMap(this.repository::save)
                .map(Item::getId)
                .flatMap(this::findOne)
                .map(newModel -> ResponseEntity.created(newModel
                        .getRequiredLink(IanaLinkRelations.SELF)
                        .toUri()).body(newModel.getContent()));
    }
}