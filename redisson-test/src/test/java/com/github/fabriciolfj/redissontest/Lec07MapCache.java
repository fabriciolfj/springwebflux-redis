package com.github.fabriciolfj.redissontest;

import com.github.fabriciolfj.redissontest.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.api.RMapReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec07MapCache extends BaseTest {

    @Test
    public void mapTest() {
        var codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapCacheReactive<Integer, Student> map = this.redisClient.getMapCache("users:cache", codec);

        Student student1 = new Student("sam", "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", "miami", List.of(10, 20, 30));

        Mono<Student> mono1 = map.put(1, student1, 5, TimeUnit.SECONDS);
        Mono<Student> mono2 = map.put(2, student2, 10, TimeUnit.SECONDS);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();

        map.get(1).doOnNext(System.out::println).subscribe();
        map.get(2).doOnNext(System.out::println).subscribe();
    }
}
