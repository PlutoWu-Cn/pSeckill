package cn.plutowu.redis;

public class AccessKey extends BasePrefix {
    /**
     * 防止被外面实例化
     */
    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 需要缓存的字段
     */
    //public static AccessKey accessKey = new AccessKey(5, "access");
    public static cn.plutowu.redis.AccessKey getExpireSeconds(int expireSeconds){
        return new cn.plutowu.redis.AccessKey(expireSeconds,"access");
    }
}
