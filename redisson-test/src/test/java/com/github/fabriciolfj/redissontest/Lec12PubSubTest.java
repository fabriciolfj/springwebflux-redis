package com.github.fabriciolfj.redissontest;

import org.junit.jupiter.api.Test;
import org.redisson.api.RPatternTopicReactive;
import org.redisson.api.RTopicReactive;
import org.redisson.api.listener.PatternMessageListener;
import org.redisson.client.codec.StringCodec;

public class Lec12PubSubTest extends BaseTest {

    @Test
    public void subscribe1() {
        final RTopicReactive topic = this.redisClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void subscribe2() {
        final RPatternTopicReactive patternTopic = this.redisClient.getPatternTopic("slack-room*", StringCodec.INSTANCE);
        patternTopic.addListener(String.class, (pattern, topic, msg) -> System.out.println(pattern + " " + topic + " " + msg)).subscribe();
        sleep(600_000);
    }
}
