package cn.plutowu.service;

import cn.plutowu.entity.User;
import cn.plutowu.exception.GlobalException;
import cn.plutowu.mapper.UserMapper;
import cn.plutowu.redis.RedisService;
import cn.plutowu.redis.UserKey;
import cn.plutowu.result.CodeMsg;
import cn.plutowu.utils.MD5Util;
import cn.plutowu.utils.UUIDUtile;
import cn.plutowu.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {

    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public User getById(Long id){
        User user = redisService.get(UserKey.getById,""+id,User.class);
        if (user != null){
            return user;
        }
        user = userMapper.getById(id);
        if (user!=null){
            redisService.set(UserKey.getById,""+id,user);
        }
        return user;
    }

    //
    public boolean updatePassword(long id,String formPass,String token){
        User user = getById(id);
        if (user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        User toBeUpdate = new User();
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(formPass, user.getSalt()));
        toBeUpdate.setId(id);
        userMapper.update(toBeUpdate);
        // 更新缓存
        redisService.delete(UserKey.getById,""+id);
        user.setPassword(toBeUpdate.getPassword());
        redisService.set(UserKey.token,token,user);
        return true;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo){
        if (loginVo == null)
            throw new GlobalException(CodeMsg.SERVER_ERROR);
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        User user = getById(Long.parseLong(mobile));
//        System.out.println(user);
        if(user == null)
            throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
        //验证密码
        String dbPass = user.getPassword();
        String saltDB = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, saltDB);
        if (!calcPass.equals(dbPass)) {
            throw new GlobalException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtile.uuid();
        addCookie(response,token,user);
        return true;
    }

    /**
     * 根据token获取用户信息
     */
    public User getByToken(HttpServletResponse response, String token){
        if (StringUtils.isEmpty(token))
            return null;
        User user = redisService.get(UserKey.token,token,User.class);
        //延长有效期
        if (user != null){
            addCookie(response,token,user);
        }
        return user;
    }

    public void addCookie(HttpServletResponse response, String token, User user) {
        redisService.set(UserKey.token, token, user);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        cookie.setMaxAge(UserKey.token.expireSeconds());
        cookie.setPath("/");//设置为网站根目录
        response.addCookie(cookie);
    }
}
