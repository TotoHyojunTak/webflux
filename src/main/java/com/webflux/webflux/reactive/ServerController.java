package com.webflux.webflux.reactive;

import com.webflux.webflux.Domain.Dish;
import com.webflux.webflux.service.KitchenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController // 1
public class ServerController {
    private final KitchenService kitchen;

    public ServerController(KitchenService kitchen){ // 2
        this.kitchen = kitchen;
    }

    @GetMapping(value="/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE) // 3
    Flux<Dish> serveDishes(){
        return this.kitchen.getDishes();
    }

    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> deliverDishes(){
        return this.kitchen.getDishes().map(dish -> Dish.deliver(dish));
    }
}

// 1 @RestController는 화면 구성을 위한 템플릿을 사용하는 대신에 결과 데이터를 직렬화하고 Http 응답 본문에 직접 써서 반환하는 Rest Controller을 의미함
// 2 애플리케이션이 실행되면, 스프링은 KitchenService의 인스턴스를 찾아서 자동으로 생성자에 주입한다
// 3 @GetMapping은 /server로 향하는 Http get 요청을 serverDishes 메소드로 라우팅해주는 스프링 웹 MVC 애너테이션이다.
// 반환되는 미디어 타입은 text/event-stream이고 클라이언트는 서버가 반환하는 스트림을 쉽게 consumer(소비)할 수 있다

