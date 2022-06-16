package com.webflux.webflux.service;


import com.webflux.webflux.domain.Cart;
import com.webflux.webflux.domain.CartItem;
import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.CartRepository;
import com.webflux.webflux.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AltInventoryService {

    private ItemRepository itemRepository;

    private CartRepository cartRepository;

    public AltInventoryService(ItemRepository repository, CartRepository cartRepository) {
        this.itemRepository = repository;
        this.cartRepository = cartRepository;
    }

    public Mono<Cart> getCart(String cartId) {
        return this.cartRepository.findById(cartId);
    }

    public Flux<Item> getInventory() {
        return this.itemRepository.findAll();
    }

    Mono<Item> saveItem(Item newItem) {
        return this.itemRepository.save(newItem);
    }

    Mono<Void> deleteItem(String id) {
        return this.itemRepository.deleteById(id);
    }

    // tag::blocking[]
    public Mono<Cart> addItemToCart(String cartId, String itemId) {
        Cart myCart = this.cartRepository.findById(cartId) //
                .defaultIfEmpty(new Cart(cartId)) //
                .block();

        return myCart.getCartItems().stream() //
                .filter(cartItem -> cartItem.getItem().getId().equals(itemId)) //
                .findAny() //
                .map(cartItem -> {
                    cartItem.increment();
                    return Mono.just(myCart);
                }) //
                .orElseGet(() -> this.itemRepository.findById(itemId) //
                        .map(item -> new CartItem(item)) //
                        .map(cartItem -> {
                            myCart.getCartItems().add(cartItem);
                            return myCart;
                        })) //
                .flatMap(cart -> this.cartRepository.save(cart));
    }
    // end::blocking[]

    Mono<Cart> removeOneFromCart(String cartId, String itemId) {
        return this.cartRepository.findById(cartId) //
                .defaultIfEmpty(new Cart(cartId)) //
                .flatMap(cart -> cart.getCartItems().stream() //
                        .filter(cartItem -> cartItem.getItem().getId().equals(itemId)) //
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.decrement();
                            return Mono.just(cart);
                        }) //
                        .orElse(Mono.empty())) //
                .map(cart -> new Cart(cart.getId(), cart.getCartItems().stream() //
                        .filter(cartItem -> cartItem.getQuantity() > 0) //
                        .collect(Collectors.toList()))) //
                .flatMap(cart -> this.cartRepository.save(cart));
    }
}
// end::code[]