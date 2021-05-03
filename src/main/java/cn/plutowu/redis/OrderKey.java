package cn.plutowu.redis;

public class OrderKey extends BasePrefix {

    public OrderKey(String prefix) {
        super(prefix);
    }

    public static cn.plutowu.redis.OrderKey getSeckillOrderByUidGid = new cn.plutowu.redis.OrderKey("seckill");
}
