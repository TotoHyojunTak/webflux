package com.webflux.webflux.ch4;

import static org.assertj.core.api.Assertions.*;

import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@DataMongoTest // <1>
public class MongoDbSliceTest {

    @Autowired
    ItemRepository repository; // <2>

    @Test // <3>
    void itemRepositorySavesItems() {
        Item sampleItem = new Item( //
                "name", "description", 1.99);

        repository.save(sampleItem) //
                .as(StepVerifier::create) //
                .expectNextMatches(item -> {
                    assertThat(item.getId()).isNotNull();
                    assertThat(item.getName()).isEqualTo("name");
                    assertThat(item.getDescription()).isEqualTo("description");
                    assertThat(item.getPrice()).isEqualTo(1.99);

                    return true;
                }) //
                .verifyComplete();
    }
}