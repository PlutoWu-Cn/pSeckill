package cn.plutowu.vo;

import cn.plutowu.entity.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 货物
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {

    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double seckillPrice;
    private Integer version;

}
