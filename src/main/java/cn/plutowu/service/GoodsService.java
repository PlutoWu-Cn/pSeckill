package cn.plutowu.service;

import cn.plutowu.entity.SeckillGoods;
import cn.plutowu.mapper.GoodsMapper;
import cn.plutowu.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    public List<GoodsVo> listGoodsVo(){
        return goodsMapper.listGoodsVo();
    }

    public GoodsVo getGoodsVoById(long goodsId){
        return goodsMapper.getGoodsVoById(goodsId);
    }

    public boolean reduceStock(GoodsVo goods) {
        int result = 0;
        SeckillGoods g = new SeckillGoods();
        g.setGoodsId(goods.getId());
        result  = goodsMapper.reduceStock(g);
        return result > 0;
    }
}
