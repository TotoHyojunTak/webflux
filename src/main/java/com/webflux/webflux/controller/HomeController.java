package com.webflux.webflux.controller;

import com.webflux.webflux.domain.Cart;
import com.webflux.webflux.domain.CartItem;
import com.webflux.webflux.domain.Item;
import com.webflux.webflux.repository.CartRepository;
import com.webflux.webflux.repository.ItemRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller //1
public class HomeController {
    //@GetMapping //2
    //Mono<String> home(){//3
    //    return Mono.just("home");//4
    //}
    //1 @Controller는 JSON이나 XML 같은 데이터가 아닌 템플릿을 사용한 웹 페이지를 반환하는 스프링 엡 컨트롤러
    //2 @GetMapping은 GET요청을 처리함을 나타내는 애너테이션. 사이트 초기 홈 화면은 아무런 하위 경로가 없으므로 아무런 경로도 명시하지 않았고 이 때 기본값은 /이다
    //3 반환 타입 Mono<String>은 템플릿의 이름을 나타내는 문자열을 리액티브 컨테이너인 Mono에 담아서 반환한다
    //4 메소드는 홈 화면 템플릿을 나타내는 home이라는 문자열을 Mono.just()로 감싸서 반환한다


    private ItemRepository itemRepository;
    private CartRepository cartRepository;

    public HomeController(ItemRepository itemRepository, // <2>
                          CartRepository cartRepository) {
        this.itemRepository = itemRepository;
        this.cartRepository = cartRepository;
    }
    // end::1[]

    // tag::2[]
    @GetMapping("/home")
    Mono<Rendering> home() { // <1>
        return Mono.just(Rendering.view("home.html") // <2>
                .modelAttribute("items", //
                        this.itemRepository.findAll()) // <3>
                .modelAttribute("cart", //
                        this.cartRepository.findById("My Cart") // <4>
                                .defaultIfEmpty(new Cart("My Cart")))
                .build());
    }
    // end::2[]

    // tag::3[]
    @PostMapping("/add/{id}") // <1>
    Mono<String> addToCart(@PathVariable String id) { // <2>
       return this.cartRepository.findById("My Cart") //
                .defaultIfEmpty(new Cart("My Cart")) // <3>
                .flatMap(cart -> cart.getCartItems().stream() // <4>
                        .filter(cartItem -> cartItem.getItem() //
                                .getId().equals(id)) //
                        .findAny() //
                        .map(cartItem -> {
                            cartItem.increment();
                            return Mono.just(cart);
                        }) //
                        .orElseGet(() -> { // <5>
                            return this.itemRepository.findById(id) //
                                    .map(item -> new CartItem(item)) //
                                    .map(cartItem -> {
                                        cart.getCartItems().add(cartItem);
                                        return cart;
                                    });
                        }))
                .flatMap(cart -> this.cartRepository.save(cart)) // <6>
                .thenReturn("redirect:/"); // <7>
        // 개선버전
        /*return this.cartService.addToCart("My Cart", id)
                .thenReturn("redirect:/");*/
    }
    // end::3[]

    @PostMapping
    Mono<String> createItem(@ModelAttribute Item newItem) {
        return this.itemRepository.save(newItem) //
                .thenReturn("redirect:/");
    }

    @DeleteMapping("/delete/{id}")
    Mono<String> deleteItem(@PathVariable String id) {
        return this.itemRepository.deleteById(id) //
                .thenReturn("redirect:/");
    }
}

