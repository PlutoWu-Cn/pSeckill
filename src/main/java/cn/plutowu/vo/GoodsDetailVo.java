package cn.plutowu.vo;

import cn.plutowu.entity.User;
import lombok.Data;

/**
 * 货物详细
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Data
public class GoodsDetailVo {

    private int seckillStatus = 0;
    private int remainSeconds = 0;
    private User user;
    private cn.plutowu.vo.GoodsVo goods;

}
