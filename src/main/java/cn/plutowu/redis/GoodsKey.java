package cn.plutowu.redis;

public class GoodsKey extends cn.plutowu.redis.BasePrefix {
    /**
     * 防止被外面实例化
     */
    private GoodsKey(int expireSeconds,String prefix) {
        super(expireSeconds,prefix);
    }

    /**
     * 需要缓存的字段
     */
    public static cn.plutowu.redis.GoodsKey getGoodList = new cn.plutowu.redis.GoodsKey(60,"lg");
    public static cn.plutowu.redis.GoodsKey getSeckillStock = new cn.plutowu.redis.GoodsKey(0,"stock");
}
