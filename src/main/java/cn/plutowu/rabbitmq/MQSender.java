package cn.plutowu.rabbitmq;

import cn.plutowu.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {

    private static Logger log = LoggerFactory.getLogger(cn.plutowu.rabbitmq.MQSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(cn.plutowu.rabbitmq.SeckillMessage seckillMessage) {
        String msg = RedisService.beanToString(seckillMessage);
        log.info("send message:"+msg); // 打印日志
        amqpTemplate.convertAndSend(cn.plutowu.rabbitmq.MQConfig.Seckill_QUEUE,msg);
    }
}
