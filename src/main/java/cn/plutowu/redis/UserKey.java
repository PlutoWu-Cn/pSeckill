package cn.plutowu.redis;

public class UserKey extends BasePrefix{
    public static final int TOKEN_EXPIRE = 3600*24 *2;//默认两天

    /**
     * 防止被外面实例化
     */
    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    /**
     * 需要缓存的字段
     */
    public static cn.plutowu.redis.UserKey token = new cn.plutowu.redis.UserKey(TOKEN_EXPIRE,"token");
    public static cn.plutowu.redis.UserKey getById = new cn.plutowu.redis.UserKey(0, "id");
}
