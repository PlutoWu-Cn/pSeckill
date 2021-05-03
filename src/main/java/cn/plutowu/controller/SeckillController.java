package cn.plutowu.controller;

import cn.plutowu.config.AccessLimit;
import cn.plutowu.entity.SeckillOrder;
import cn.plutowu.entity.User;
import cn.plutowu.rabbitmq.MQSender;
import cn.plutowu.rabbitmq.SeckillMessage;
import cn.plutowu.redis.GoodsKey;
import cn.plutowu.redis.RedisService;
import cn.plutowu.result.CodeMsg;
import cn.plutowu.result.Result;
import cn.plutowu.service.GoodsService;
import cn.plutowu.service.OrderService;
import cn.plutowu.service.SeckillService;
import cn.plutowu.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 秒杀控制器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SeckillService seckillService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private MQSender mqSender;

    // 本地标识,减少redis访问
    private final Map<Long,Boolean> localOverMap = new HashMap<>();

    /**
    * 后置处理器：初始化redis缓存
    * */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> goodsVoList = goodsService.listGoodsVo();
        for (GoodsVo goodsVo : goodsVoList) {
            redisService.set(GoodsKey.getSeckillStock,""+goodsVo.getId(),goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
    }

    @RequestMapping(value = "/{path}/do_seckill",method = RequestMethod.POST)
    @ResponseBody
    public Result<Integer> doSeckill(Model model, User user, @RequestParam("goodsId") long goodsId, @PathVariable String path){
        model.addAttribute("user",user);
        if (user == null)
            return Result.error(CodeMsg.SESSION_ERROR);
        // 验证path
        boolean check = seckillService.checkPath(user,goodsId,path);
        if (!check)
            return Result.error(CodeMsg.REQUEST_ILLEGAL);

        boolean over = localOverMap.get(goodsId);
        if (over)
            return Result.error(CodeMsg.SECKILL_OVER);
        Long count = redisService.decr(GoodsKey.getSeckillStock,""+goodsId);
        if (count < 0){
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.SECKILL_OVER);
        }
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(user.getId(),goodsId);
        if (order != null)
            return Result.error(CodeMsg.REPEATE_SECKILL);
        SeckillMessage seckillMessage = new SeckillMessage();
        seckillMessage.setUser(user);
        seckillMessage.setGoodsId(goodsId);
        mqSender.sendSeckillMessage(seckillMessage);
        return Result.success(0);
    }

    /**
     * orderId：成功
     * -1：秒杀失败
     * 0： 排队中
     */
    @AccessLimit(seconds=5,maxCount=10,needLogin=true)
    @RequestMapping(value = "/result", method = RequestMethod.GET)
    @ResponseBody
    public Result<Long> seckillResult(User user,@RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId = seckillService.getSeckillResult(user.getId(), goodsId);
        return Result.success(orderId);
    }

    /**
    * 隐藏地址
    **/
    @AccessLimit(seconds=5,maxCount=5,needLogin=true)
    @ResponseBody
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    public Result<String> getSeckillPath(User user,@RequestParam("goodsId")long goodsId){
        if(user == null)
            return Result.error(CodeMsg.SESSION_ERROR);

        String path = seckillService.createPaht(user,goodsId);
        return Result.success(path);
    }



}
