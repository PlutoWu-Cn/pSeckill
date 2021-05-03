package cn.plutowu.config;

import cn.plutowu.entity.User;
import cn.plutowu.redis.AccessKey;
import cn.plutowu.redis.RedisService;
import cn.plutowu.result.CodeMsg;
import cn.plutowu.service.UserService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 访问拦截器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            User user = getUser(request,response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod)handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (accessLimit == null)
                return true;
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin){
                if (user == null){
                    render(response, CodeMsg.SESSION_ERROR);
                    return false;
                }
                key += "_"+ user.getId();
            }
            Integer count = redisService.get(AccessKey.getExpireSeconds(seconds),key,Integer.class);
            if (count == null){
                redisService.set(AccessKey.getExpireSeconds(seconds),key,1);
            }else if (count < maxCount){
                redisService.incr(AccessKey.getExpireSeconds(seconds),key);
            }else{
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, CodeMsg sessionError) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(sessionError);
        out.write(str.getBytes("utf-8"));
        out.flush();
        out.close();
    }

    /**
     * 先获取到已有参数HttpServletRequest，从中获取到token，再用token作为key从redis拿到User，
     * 而HttpServletResponse作用是为了延迟有效期
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String paramToken = request.getParameter(userService.COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request,userService.COOKIE_NAME_TOKEN);

        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken))
            return null;
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        return userService.getByToken(response,token);
    }

    // 遍历Cookie
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length <= 0) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
