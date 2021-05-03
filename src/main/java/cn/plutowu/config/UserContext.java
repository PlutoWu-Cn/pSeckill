package cn.plutowu.config;


import cn.plutowu.entity.User;

/**
 * 用户上下文
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
public class UserContext {

    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    public static void setUser(User user){
        userHolder.set(user);
    }

    public static User getUser(){
        return userHolder.get();
    }

}
