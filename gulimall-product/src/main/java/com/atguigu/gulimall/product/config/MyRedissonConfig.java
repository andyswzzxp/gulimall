package com.atguigu.gulimall.product.config;//package com.atguigu.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.IOException;



@Configuration
public class MyRedissonConfig {
    //private RedissonClient client;


    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException
    {
        Config config=new Config();
        config.useSingleServer().setAddress("redis://192.168.11.238:6379");
        RedissonClient  redissonClient= Redisson.create(config);
        return  redissonClient;
    }


//     public RedissonClient redissonClient() {
//                Config config = new Config();
//                config.useClusterServers()
//                        .setScanInterval(2000)
//                        .addNodeAddress("redis://10.0.29.30:6379", "redis://10.0.29.95:6379")
//                         .addNodeAddress("redis://10.0.29.205:6379");
//
//                 RedissonClient redisson = Redisson.create(config);
//
//                 return redisson;
//             }
}
