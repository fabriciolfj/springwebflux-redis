package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec04asMapTest extends BaseTest {
    
    @Test
    public void testBucketAsMap() {
        var bucket = this.redisClient.getBuckets(StringCodec.INSTANCE).get("user:1:name", "user:2:name")
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(bucket)
                .verifyComplete();
    }
}
