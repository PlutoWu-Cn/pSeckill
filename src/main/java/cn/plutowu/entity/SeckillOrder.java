package cn.plutowu.entity;

import lombok.Data;

/**
 * 秒杀订单
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class SeckillOrder {

    private Long id;
    private Long userId;
    private Long orderId;
    private Long goodsId;

}
