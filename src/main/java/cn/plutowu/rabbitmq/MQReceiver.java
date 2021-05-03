package cn.plutowu.rabbitmq;

import cn.plutowu.entity.SeckillOrder;
import cn.plutowu.entity.User;
import cn.plutowu.redis.RedisService;
import cn.plutowu.service.GoodsService;
import cn.plutowu.service.OrderService;
import cn.plutowu.service.SeckillService;
import cn.plutowu.vo.GoodsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver{

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private OrderService orderService;

    private static Logger log = LoggerFactory.getLogger(cn.plutowu.rabbitmq.MQReceiver.class);

    @RabbitListener(queues = MQConfig.Seckill_QUEUE)
    public void receive(String message){
        log.info("receive message:"+message);
        SeckillMessage seckillMessage = RedisService.stringToBean(message, cn.plutowu.rabbitmq.SeckillMessage.class);
        long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.getGoodsVoById(goodsId);
        int stock = goodsVo.getStockCount();
        if (stock <= 0){
            return;
        }
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order != null){
            return;
        }
        seckillService.seckill(user,goodsVo);
    }

}
