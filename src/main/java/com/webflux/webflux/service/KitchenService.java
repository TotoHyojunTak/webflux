package com.webflux.webflux.service;

import com.webflux.webflux.domain.Dish;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service // 1
public class KitchenService {
    /*
        요리 스트림 생성
     */
    public Flux<Dish> getDishes(){ // 2
        return Flux.<Dish> generate(sink -> sink.next(randomDish()))//
                .delayElements(Duration.ofMillis(250));
    }

    /*
        요리 무작위 선택
     */
    private Dish randomDish(){
        return menu.get(picker.nextInt(menu.size()));
    }

    private List<Dish> menu = Arrays.asList(//
            new Dish("sesame chicken"),//
            new Dish("lo mein noodlesm plain"),//
            new Dish("Sweet & sour beef"));

    private Random picker = new Random();

}

// 1 @Service는 스프링 빈임을 나타내는 애너테이션이며 컴포넌트 탐색 과정에서 스프링 부트에 의해 자동으로 생성되는 빈으로 등록되고, 필요하다면 ServerController에 주입된다
// 2 getDishes()는 하드 코딩을 사용했던 기존 코딩과 동일한 메소드 시그니처를 가지고 있지만 3개 요리만 제공했던 기존 코드와 다르게 세 가지 요리 중에서 무작위로 선택된 1개의 요리를 250밀리초 간격으로 계속 제공한다