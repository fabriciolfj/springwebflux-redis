package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RDequeReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lect09ListQueueStackTest extends BaseTest {

    @Test
    public void listTest() {
        RListReactive<Long> list = this.redisClient.getList("number-input", LongCodec.INSTANCE);

        Mono<Void> listAdd = Flux.range(1, 10)
                .map(Long::valueOf)
                .flatMap(list::add)
                .then();

        StepVerifier.create(listAdd)
                .verifyComplete();

        StepVerifier.create(list.size())
                .expectNext(10)
                .verifyComplete();
    }

    @Test
    public void queueTest() {
        RQueueReactive<Long> queue = this.redisClient.getQueue("number-input", LongCodec.INSTANCE);
        Mono<Void> queuePoll = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(queue.size())
                .expectNext(6)
                .verifyComplete();
    }

    @Test
    public void dequeueTest() {
        RDequeReactive<Long> dequeue = this.redisClient.getDeque("number-input", LongCodec.INSTANCE);
        Mono<Void> queuePoll = dequeue.pollLast()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

        StepVerifier.create(dequeue.size())
                .expectNext(2)
                .verifyComplete();
    }
}
