package cn.plutowu.mapper;

import cn.plutowu.entity.SeckillGoods;
import cn.plutowu.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 货物映射器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Mapper
public interface GoodsMapper {

    @Select("select g.*,sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price from seckill_goods sg left join goods g on sg.goods_id = g.id")
    public List<GoodsVo> listGoodsVo();

    @Select("select g.*, sg.stock_count, sg.start_date, sg.end_date, sg.seckill_price from seckill_goods sg left join goods g on sg.goods_id = g.id WHERE g.id = #{goodsId}")
    public GoodsVo getGoodsVoById(@Param("goodsId") long goodsId);

    @Update("update seckill_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count > 0")
    public int reduceStock(SeckillGoods g);
}
