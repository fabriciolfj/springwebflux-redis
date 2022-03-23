package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLongReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class Lect03TestNumber extends BaseTest {

    @Test
    public void testIncrementNumber() {
        final RAtomicLongReactive atomicLong = this.redisClient.getAtomicLong("user:1:visit");

        Mono<Void> mono = Flux.range(1, 30)
                .delayElements(Duration.ofSeconds(1))
                .flatMap(v -> atomicLong.incrementAndGet())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
