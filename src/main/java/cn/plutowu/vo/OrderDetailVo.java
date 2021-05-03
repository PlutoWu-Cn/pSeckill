package cn.plutowu.vo;

import cn.plutowu.entity.Order;
import lombok.Data;

/**
 * 订单的详细信息
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class OrderDetailVo {

    private cn.plutowu.vo.GoodsVo goods;
    private Order order;

}
