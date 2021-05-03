package cn.plutowu.controller;

import cn.plutowu.entity.User;
import cn.plutowu.redis.RedisService;
import cn.plutowu.result.Result;
import cn.plutowu.service.GoodsService;
import cn.plutowu.vo.GoodsDetailVo;
import cn.plutowu.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 产品控制器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping("/to_list")
    //自定义参数解析器，可以从redis里拿到user对象
    public String toList(Model model, User user, HttpServletRequest request, HttpServletResponse response){
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsList);
        return "goods_list";
    }

    //做了页面静态化
    @RequestMapping("/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(Model model, User user, @PathVariable("goodsId")long goodsId){
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {//秒杀未开始
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime){//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else {//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        GoodsDetailVo goodsDetailVo = new GoodsDetailVo();
        goodsDetailVo.setGoods(goods);
        goodsDetailVo.setSeckillStatus(seckillStatus);
        goodsDetailVo.setRemainSeconds(remainSeconds);
        goodsDetailVo.setUser(user);
        return Result.success(goodsDetailVo);
    }

    //没有做页面静态化的
    @RequestMapping("/to_detail/{goodsId}")
    public String detail2(Model model, User user, @PathVariable("goodsId")long goodsId){
        model.addAttribute("user",user);
        //根据id查
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        model.addAttribute("goods",goods);

        long startTime = goods.getStartDate().getTime();
        long endTime = goods.getEndDate().getTime();
        long now = System.currentTimeMillis();

        int seckillStatus = 0;
        int remainSeconds = 0;
        if (now < startTime) {//秒杀未开始
            seckillStatus = 0;
            remainSeconds = (int) ((startTime - now) / 1000);
        } else if (now > endTime){//秒杀已经结束
            seckillStatus = 2;
            remainSeconds = -1;
        }else{//秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }
        model.addAttribute("seckillStatus",seckillStatus);
        model.addAttribute("remainSeconds",remainSeconds);
        return "goods_detail";
    }
}
