package cn.plutowu.service;

import cn.plutowu.entity.Order;
import cn.plutowu.entity.SeckillOrder;
import cn.plutowu.entity.User;
import cn.plutowu.redis.RedisService;
import cn.plutowu.redis.SeckillKey;
import cn.plutowu.utils.MD5Util;
import cn.plutowu.utils.UUIDUtile;
import cn.plutowu.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeckillService {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RedisService redisService;

    @Transactional
    public Order seckill(User user, GoodsVo goods) {
        // 减库存
        boolean success = goodsService.reduceStock(goods);
        if (success){
            // 生成订单
            return orderService.createOrder(user,goods);
        }else{
            setGoodsOver(goods.getId());
            return null;
        }

    }

    private void setGoodsOver(Long id) {
        redisService.set(SeckillKey.isGoodsOver,""+id,true);
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        if (order != null){//秒杀成功
            return order.getOrderId();
        }else{
            boolean isOver = getGoodsOver(goodsId);
            if (isOver)
                return -1;
            else
                return 0;
        }
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(SeckillKey.isGoodsOver,""+goodsId);
    }

    public boolean checkPath(User user, long goodsId, String path) {
        if (user == null || path == null)
            return false;
        String oldPath= redisService.get(SeckillKey.hiddenPath,""+goodsId+""+user.getId(),String.class);
        return path.equals(oldPath);
    }

    public String createPaht(User user,long goodsId) {
        String str = MD5Util.md5(UUIDUtile.uuid()+"123456");
        redisService.set(SeckillKey.hiddenPath,""+goodsId+""+user.getId(),str);
        return str;
    }
}
