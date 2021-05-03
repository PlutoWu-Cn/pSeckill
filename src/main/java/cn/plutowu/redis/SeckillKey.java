package cn.plutowu.redis;

public class SeckillKey extends BasePrefix{
    /**
     * 防止被外面实例化
     */
    private SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds,prefix);
    }

    /**
     * 需要缓存的字段
     */
    public static cn.plutowu.redis.SeckillKey isGoodsOver = new cn.plutowu.redis.SeckillKey(0,"is Over");
    public static cn.plutowu.redis.SeckillKey hiddenPath = new cn.plutowu.redis.SeckillKey(60,"hiddenPath");
}
