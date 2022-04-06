package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.*;
import org.redisson.api.RBucketReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec14TransactionTest extends BaseTest {

    private RBucketReactive<Long> user1Balance;
    private RBucketReactive<Long> user2Balance;

    @BeforeEach
    public void accountSetup() {
        this.user1Balance = this.redisClient.getBucket("user3:balance", LongCodec.INSTANCE);
        this.user2Balance = this.redisClient.getBucket("user4:balance", LongCodec.INSTANCE);

        Mono<Void> mono = user1Balance
                .set(100L)
                .then(user2Balance.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @AfterEach
    public void accountBalanceStatus() {
        Mono<Void> mono = Flux.zip(this.user1Balance.get(), this.user2Balance.get())
                .doOnNext(t -> System.out.println(t.getT1() + " " + t.getT2()))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void testExecute() {
        var transaction = this.redisClient.createTransaction(TransactionOptions.defaults());
        RBucketReactive<Long> user1Balance = transaction.getBucket("user3:balance", LongCodec.INSTANCE);
        RBucketReactive<Long> user2Balance = transaction.getBucket("user4:balance", LongCodec.INSTANCE);
        this.transfer(user1Balance, user2Balance, 50)
                        .doOnError(ex -> System.out.println(ex))
                        .onErrorResume(e -> {
                            System.out.println("rollback");
                            return transaction.rollback();
                        })
                        .then(transaction.commit())
                .doOnNext(System.out::println)
                .subscribe();

        sleep(1000);
    }

    public Mono<Void> transfer(RBucketReactive<Long> fromAccount, RBucketReactive<Long> toAccount, int amount) {
        return Flux.zip(fromAccount.get(), toAccount.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> fromAccount.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> toAccount.set(t.getT2() + amount))
                .then();
    }
}
