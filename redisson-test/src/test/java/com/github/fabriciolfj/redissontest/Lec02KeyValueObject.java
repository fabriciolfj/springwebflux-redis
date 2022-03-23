package com.github.fabriciolfj.redissontest;

import com.github.fabriciolfj.redissontest.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

public class Lec02KeyValueObject extends BaseTest {

    @Test
    public void testKeyValueObject() {
        final var student = new Student("fabricio", "serrana", Arrays.asList(1,2,3));
        final RBucketReactive<Student> bucket = this.redisClient.getBucket("student:1", new TypedJsonJacksonCodec(Student.class));
        final Mono<Void> set = bucket.set(student);
        final Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }
}
