package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Lec10Loglog extends BaseTest {

    @Test
    public void testLog() {
        RHyperLogLogReactive<Long> counter = this.redisClient.getHyperLogLog("user:visits", LongCodec.INSTANCE);

        var list1 = LongStream.range(1, 25_000)
                .boxed()
                .collect(Collectors.toList());

        var list2 = LongStream.range(25_001, 50_000)
                .boxed()
                .collect(Collectors.toList());

        var mono = Flux.just(list1, list2)
                .flatMap(counter::addAll)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        counter.count()
                .doOnNext(System.out::println)
                .subscribe();
    }
}
