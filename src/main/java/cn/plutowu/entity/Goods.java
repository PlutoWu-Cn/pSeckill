package cn.plutowu.entity;

import lombok.Data;

/**
 * 货物
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class Goods {

    private Long id;
    private String goodsName;
    private String goodsTitle;
    private String goodsImg;
    private String goodsDetail;
    private Double goodsPrice;
    private Integer goodsStock;

}
