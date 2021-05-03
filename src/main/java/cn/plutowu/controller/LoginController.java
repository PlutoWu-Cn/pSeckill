package cn.plutowu.controller;

import cn.plutowu.result.Result;
import cn.plutowu.service.UserService;
import cn.plutowu.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * 登录控制器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired(required = false)
    private UserService userService;


    private static Logger log = LoggerFactory.getLogger(cn.plutowu.controller.LoginController.class);

    @RequestMapping("/to_login")
    public String toLogin(){
        return "login";
    }

    @RequestMapping("/do_login")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo){
        log.info(loginVo.toString());
        //登陆
        userService.login(response,loginVo);
        return Result.success(true);
    }

}
