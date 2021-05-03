package cn.plutowu.controller;

import cn.plutowu.entity.Order;
import cn.plutowu.entity.User;
import cn.plutowu.result.CodeMsg;
import cn.plutowu.result.Result;
import cn.plutowu.service.GoodsService;
import cn.plutowu.service.OrderService;
import cn.plutowu.vo.GoodsVo;
import cn.plutowu.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 订单控制器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/detail")
    @ResponseBody
    public Result<OrderDetailVo> info(Model model, User user, @RequestParam("orderId")long orderId){
        if(user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Order order = orderService.getOrderById(orderId);
        if(order == null) {
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoById(goodsId);
        OrderDetailVo vo = new OrderDetailVo();
        vo.setOrder(order);
        vo.setGoods(goods);
        return Result.success(vo);
    }
}
