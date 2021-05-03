package cn.plutowu.entity;

import lombok.Data;

import java.util.Date;

/**
 * 秒杀商品
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class SeckillGoods {

    private Long id;
    private Long goodsId;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private int version;

}

