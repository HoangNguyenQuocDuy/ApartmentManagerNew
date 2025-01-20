package hnqd.aparmentmanager.authservice.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRabbit
public class RedisConfig {

    // create StringRedisTemplate using to connect Redis
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer()); // Key as String
        template.setValueSerializer(new StringRedisSerializer()); // Value as String
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    // config connection pool
    //    Better performance: Reuse connections instead of creating new ones each time
    //    Reduce load on Redis server: Limit unnecessary concurrent connections
    //    Optimize resources: Limit maximum number of connections, ensuring the system is not overloaded.
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
//        redisConfig.setHostName("localhost");
//        redisConfig.setPort(6379);
////        redisConfig.setPassword("KhoaLuanTotNghiep"); // password
//
//        LettucePoolingClientConfiguration poolConfig = LettucePoolingClientConfiguration.builder()
//                .commandTimeout(java.time.Duration.ofSeconds(2))
//                .build();
//
//        return new LettuceConnectionFactory(redisConfig, poolConfig);
//    }
}
