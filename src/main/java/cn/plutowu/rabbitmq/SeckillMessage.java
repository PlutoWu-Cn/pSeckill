package cn.plutowu.rabbitmq;

import cn.plutowu.entity.User;
import lombok.Data;

@Data
public class SeckillMessage {

    private User user;
    private long goodsId;

}
