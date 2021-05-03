package cn.plutowu.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {
    public static final String Seckill_QUEUE = "seckillQueue";

    @Bean
    public Queue seckillQueue(){
        return new Queue(Seckill_QUEUE,true);
    }
}
