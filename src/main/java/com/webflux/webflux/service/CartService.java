package com.webflux.webflux.service;

import com.webflux.webflux.domain.Cart;
import com.webflux.webflux.domain.CartItem;
import com.webflux.webflux.repository.CartRepository;
import com.webflux.webflux.repository.ItemRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CartService {
    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;

    CartService(ItemRepository itemRepository, CartRepository cartRepository) { // <2>
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }

    Mono<Cart> addToCart(String cartId, String id) { // <3>
        return this.cartRepository.findById(cartId) //
                .defaultIfEmpty(new Cart(cartId)) //
                .flatMap(cart -> cart.getCartItems().stream() //
                        .filter(cartItem -> cartItem.getItem() //
                                .getId().equals(id)) //
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        }) //
                        .orElseGet(() -> //
                                this.itemRepository.findById(id) //
                                        .map(CartItem::new) // <4>
                                        .doOnNext(cartItem -> //
                                                cart.getCartItems().add(cartItem)) //
                                        .map(cartItem -> cart)))
                .flatMap(this.cartRepository::save); // <5>
    }
}
