package com.webflux.webflux.repository;

import com.webflux.webflux.domain.Item;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ItemRepository extends ReactiveCrudRepository<Item, String> {

    Flux<Item> findByNameContaining(String partialName);

    @Query("{ 'name' : ?0, 'age' :  }")
    Flux<Item> findItemsForCustomerMonthlyReport();

    @Query(sort = "{ 'age' : -1", value = "{ 'name' : 'TV tray', 'age' : }")
    Flux<Item> findSortedStuffForWeeklyReport();

}