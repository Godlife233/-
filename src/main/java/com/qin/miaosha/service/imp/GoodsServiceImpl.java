package com.qin.miaosha.service.imp;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.GoodsDao;
import com.qin.miaosha.domain.Goods;
import com.qin.miaosha.domain.MiaoshaGoods;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("goodsService")
public class GoodsServiceImpl implements GoodsService{

    @Autowired
    GoodsDao goodsDao ;
     public ServerResponse<List<GoodsVo>> listGoodsVo(){
         List<GoodsVo> goodsVoList = goodsDao.getGoodsVoList();
         if(goodsVoList!=null)
         return ServerResponse.createBySuccess(goodsDao.getGoodsVoList());
         else {
             return ServerResponse.createBySuccessMessage("现在没有商品参与秒杀");
         }
     }

     public ServerResponse<GoodsVo> getById(long id){
        GoodsVo goodsVo = goodsDao.getById(id);
        if(goodsVo==null){
            return ServerResponse.createBySuccessMessage("该商品不存在");
        }
        else {
            return ServerResponse.createBySuccess(goodsVo);
        }
     }

     public boolean reduceStock(GoodsVo goodsVo){
         int ret =goodsDao.reduceStock(goodsVo.getId());
        return ret>0;
     }

}
