package com.webflux.webflux.config;

import com.webflux.webflux.domain.Item;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * @author Greg Turnquist
 */
// tag::code[]
@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(MongoOperations mongo) {
        return args -> {
            mongo.save(new Item("Alf alarm clock", "kids clock", 19.99));
            mongo.save(new Item("Smurf TV tray", "kids TV tray", 24.99));
        };
    }
}
// end::code[]