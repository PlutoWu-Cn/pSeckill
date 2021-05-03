package cn.plutowu.mapper;

import cn.plutowu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 用户映射器
 *
 * @author PlutoWu
 * @date 2021/05/01
 */
@Mapper
public interface UserMapper {
    @Select("select * from seckill_user where id = #{id}")
    public User getById(@Param("id") long id);

    @Update("update seckill_user set password = #{password} where id = #{id}")
    public void update(User toBeUpdate);
}
