package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec01KeyValueTest extends BaseTest {

    @Test
    public void testKeyValueAccess() {
        final RBucketReactive<String> bucket = this.redisClient.getBucket("user:1:name", StringCodec.INSTANCE);
        final Mono<Void> set = bucket.set("fabricio", 10, TimeUnit.SECONDS);
        final Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void testKeyValueExtendExpiryTest() {
        final RBucketReactive<String> bucket = this.redisClient.getBucket("user:1:name", StringCodec.INSTANCE);
        final Mono<Void> set = bucket.set("fabricio", 10, TimeUnit.SECONDS);
        final Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        sleep(5000);
        Mono<Boolean> mono = bucket.expire(60, TimeUnit.SECONDS);
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
