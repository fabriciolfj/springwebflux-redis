package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.api.RSetReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec11BatchTest extends BaseTest {

    @Test
    public void regularTest() {
        RListReactive<Long> list = this.redisClient.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = this.redisClient.getSet("numbers-set", LongCodec.INSTANCE);
        Mono<Void> mono = Flux.range(1, 500_000)
                .map(Long::valueOf)
                .flatMap(i -> list.add(i).then(set.add(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
