package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class Lec05EventListener extends BaseTest {

    @Test
    public void expiredEventTest() {
        RBucketReactive<String> bucket = this.redisClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", 4, TimeUnit.SECONDS);
        Mono<Void> get  = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> event = bucket.addListener((ExpiredObjectListener) s -> System.out.println("Expired: " + s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        sleep(11000);

    }

    @Test
    public void deleteEventTest() {
        RBucketReactive<String> bucket = this.redisClient.getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("sam", 4, TimeUnit.SECONDS);
        Mono<Void> get  = bucket.get()
                .doOnNext(System.out::println)
                .then();

        Mono<Void> event = bucket.addListener((DeletedObjectListener) s -> System.out.println("Deleted: " + s)).then();

        StepVerifier.create(set.concatWith(get).concatWith(event))
                .verifyComplete();

        sleep(11000);

    }
}
