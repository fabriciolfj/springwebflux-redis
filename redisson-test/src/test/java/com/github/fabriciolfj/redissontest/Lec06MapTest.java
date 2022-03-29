package com.github.fabriciolfj.redissontest;

import com.github.fabriciolfj.redissontest.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class Lec06MapTest extends BaseTest {

    @Test
    public void mapTest() {
        RMapReactive<String, String> map = this.redisClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "sam");
        Mono<String> age = map.put("age", "10");
        Mono<String> atlanta = map.put("city", "atlanta");

        StepVerifier.create(name.concatWith(age).concatWith(atlanta).then())
                .verifyComplete();
    }

    @Test
    public void mapJavaTest() {
        RMapReactive<String, String> map = this.redisClient.getMap("user:2", StringCodec.INSTANCE);
        var mapJava = Map.of(
                "name", "fabricio",
                "age" , "37",
                "city", "serrana"
        );

        StepVerifier.create(map.putAll(mapJava).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {
        var codec = new TypedJsonJacksonCodec(Integer.class, Student.class);
        RMapReactive<Integer, Student> map = this.redisClient.getMap("users", codec);

        Student student1 = new Student("sam", "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", "miami", List.of(10, 20, 30));

        Mono<Student> mono1 = map.put(1, student1);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }
}
