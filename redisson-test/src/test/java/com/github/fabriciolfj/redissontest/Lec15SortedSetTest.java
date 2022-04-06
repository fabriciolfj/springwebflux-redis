package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.function.Function;

public class Lec15SortedSetTest extends BaseTest {

    @Test
    public void testSorted() {
        RScoredSortedSetReactive<String> sorted = this.redisClient.getScoredSortedSet("student:score", StringCodec.INSTANCE);

        Mono<Void> mono = sorted.addScore("sam", 12.25)
                .then(sorted.add(23.25, "mike"))
                .then(sorted.addScore("jake", 7))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        sorted.entryRange(0, 1)
                .flatMapIterable(Function.identity())
                .map(se -> se.getScore() + " : " + se.getValue())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(1000);
    }
}
