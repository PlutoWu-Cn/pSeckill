package cn.plutowu.service;

import cn.plutowu.entity.Order;
import cn.plutowu.entity.SeckillOrder;
import cn.plutowu.entity.User;
import cn.plutowu.mapper.OrderMapper;
import cn.plutowu.redis.OrderKey;
import cn.plutowu.redis.RedisService;
import cn.plutowu.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisService redisService;

    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId) {
        return redisService.get(OrderKey.getSeckillOrderByUidGid,""+userId+""+goodsId,SeckillOrder.class);
    }

    @Transactional
    public Order createOrder(User user, GoodsVo goods) {
        Order orderInfo = new Order();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getGoodsPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderMapper.insert(orderInfo);

        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setGoodsId(goods.getId());
        seckillOrder.setOrderId(orderInfo.getId());
        seckillOrder.setUserId(user.getId());
        orderMapper.insertSeckillOrder(seckillOrder);

        redisService.set(OrderKey.getSeckillOrderByUidGid,""+user.getId()+""+goods.getId(),seckillOrder);
        return orderInfo;
    }

    public Order getOrderById(long orderId) {

        return orderMapper.getOrderById(orderId);
    }
}
