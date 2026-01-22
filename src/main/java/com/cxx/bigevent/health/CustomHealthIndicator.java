package com.cxx.bigevent.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    private final DataSource dataSource;
    private final StringRedisTemplate redisTemplate;

    public CustomHealthIndicator(DataSource dataSource, StringRedisTemplate redisTemplate) {
        this.dataSource = dataSource;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Health health() {
        Health.Builder builder = new Health.Builder();

        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(5)) {
                return builder.down()
                    .withDetail("database", "connection invalid")
                    .build();
            }
        } catch (SQLException e) {
            return builder.down()
                .withDetail("database", e.getMessage())
                .build();
        }

        try {
            String pong = redisTemplate.getConnectionFactory().getConnection().ping();
            if (!"PONG".equals(pong)) {
                return builder.status(new Status("WARN"))
                    .withDetail("redis", "unexpected response: " + pong)
                    .build();
            }
        } catch (Exception e) {
            return builder.down()
                .withDetail("redis", e.getMessage())
                .build();
        }

        return builder.up()
            .withDetail("database", "connected")
            .withDetail("redis", "connected")
            .build();
    }
}
