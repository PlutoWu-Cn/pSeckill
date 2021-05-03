package cn.plutowu.mapper;

import cn.plutowu.entity.Order;
import cn.plutowu.entity.SeckillOrder;
import org.apache.ibatis.annotations.*;

/**
 * 订单映射器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Mapper
public interface OrderMapper {

    @Select("select * from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
    public SeckillOrder getOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("insert into orders (user_id,goods_id,goods_name,goods_count,goods_price,order_channel, status, create_date) values(#{userId}, #{goodsId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel},#{status},#{createDate})")
    @SelectKey(keyColumn = "id", keyProperty = "id", resultType = long.class, before = false, statement = "select last_insert_id()")
    public long insert(Order orderInfo);

    @Insert("insert into seckill_order (user_id,order_id,goods_id) values (#{userId},#{orderId},#{goodsId})")
    public void insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("select * from orders where id = #{orderId}")
    public Order getOrderById(@Param("orderId") long orderId);
}
