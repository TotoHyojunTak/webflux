package com.webflux.webflux.ch6;


import static org.mockito.Mockito.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.*;

import com.webflux.webflux.controller.ApiItemController;
import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.ItemRepository;
import com.webflux.webflux.service.InventoryService;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author Greg Turnquist
 */
// tag::intro[]
@WebFluxTest(controllers = ApiItemController.class) // <1>
@AutoConfigureRestDocs // <2>
public class ApiItemControllerDocumentationTest {

    @Autowired private WebTestClient webTestClient; // <3>

    @MockBean
    InventoryService service; // <4>

    @MockBean
    ItemRepository repository; // <5>
    // end::intro[]

    // tag::test1[]
    @Test
    void findingAllItems() {
        when(repository.findAll()).thenReturn( // <1>
                Flux.just(new Item("item-1", "Alf alarm clock", //
                        "nothing I really need", 19.99)));

        this.webTestClient.get().uri("/api/items") //
                .exchange() //
                .expectStatus().isOk() //
                .expectBody() //
                .consumeWith(document("findAll", preprocessResponse(prettyPrint()))); // <2>
    }
    // end::test1[]

    // tag::test2[]
    @Test
    void postNewItem() {
        when(repository.save(any())).thenReturn( //
                Mono.just(new Item("1", "Alf alarm clock", "nothing important", 19.99)));

        this.webTestClient.post().uri("/api/items") // <1>
                .bodyValue(new Item("Alf alarm clock", "nothing important", 19.99)) // <2>
                .exchange() //
                .expectStatus().isCreated() // <3>
                .expectBody() //
                .consumeWith(document("post-new-item", preprocessResponse(prettyPrint()))); // <4>
    }
    // end::test2[]

    // tag::test3[]
    @Test
    void findOneItem() {
        when(repository.findById("item-1")).thenReturn( //
                Mono.just(new Item("item-1", "Alf alarm clock", "nothing I really need", 19.99))); // <1>

        this.webTestClient.get().uri("/api/items/item-1") //
                .exchange() //
                .expectStatus().isOk() //
                .expectBody() //
                .consumeWith(document("findOne", preprocessResponse(prettyPrint()))); // <2>
    }
    // end::test3[]

    @Test
    void updateItem() {
        when(repository.save(any())).thenReturn( //
                Mono.just(new Item("1", "Alf alarm clock", "updated", 19.99)));

        this.webTestClient.put().uri("/api/items/1") // <1>
                .bodyValue(new Item("Alf alarm clock", "updated", 19.99)) // <2>
                .exchange() //
                .expectStatus().isOk() // <3>
                .expectBody() //
                .consumeWith(document("update-item", preprocessResponse(prettyPrint()))); // <4>
    }
}