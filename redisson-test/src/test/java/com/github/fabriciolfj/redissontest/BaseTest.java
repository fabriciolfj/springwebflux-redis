package com.github.fabriciolfj.redissontest;

import com.github.fabriciolfj.redissontest.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private final RedissonConfig config = new RedissonConfig();
    protected RedissonReactiveClient redisClient;

    @BeforeAll
    public void setup() {
        this.redisClient = config.getReactiveClient();
    }

    @AfterAll
    public void shutdown() {
        this.redisClient.shutdown();
    }

    protected void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
