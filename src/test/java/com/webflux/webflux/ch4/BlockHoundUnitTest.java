package com.webflux.webflux.ch4;

import static org.assertj.core.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BlockHoundUnitTest {
    // tag::obvious-failure[]
    @Test
    void threadSleepIsABlockingCall() {
        Mono.delay(Duration.ofSeconds(1)) // <1>
                .flatMap(tick -> {
                    try {
                        Thread.sleep(10); // <2>
                        return Mono.just(true);
                    } catch (InterruptedException e) {
                        return Mono.error(e);
                    }
                }) //
                .as(StepVerifier::create) //
                .verifyComplete();
//				.verifyErrorMatches(throwable -> {
//					assertThat(throwable.getMessage()) //
//							.contains("Blocking call! java.lang.Thread.sleep");
//					return true;
//				});

    }
    // end::obvious-failure[]
}
