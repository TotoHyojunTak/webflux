package com.webflux.webflux.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@Controller //1
public class HomController {
    @GetMapping //2
    Mono<String> home(){//3
        return Mono.just("home");//4
    }
}

//1 @Controller는 JSON이나 XML 같은 데이터가 아닌 템플릿을 사용한 웹 페이지를 반환하는 스프링 엡 컨트롤러
//2 @GetMapping은 GET요청을 처리함을 나타내는 애너테이션. 사이트 초기 홈 화면은 아무런 하위 경로가 없으므로 아무런 경로도 명시하지 않았고 이 때 기본값은 /이다
//3 반환 타입 Mono<String>은 템플릿의 이름을 나타내는 문자열을 리액티브 컨테이너인 Mono에 담아서 반환한다
//4 메소드는 홈 화면 템플릿을 나타내는 home이라는 문자열을 Mono.just()로 감싸서 반환한다
