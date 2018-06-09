package com.qin.miaosha.service.imp;

import com.qin.miaosha.dao.GoodsDao;
import com.qin.miaosha.domain.Goods;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;
import com.qin.miaosha.domain.OrderInfo;
import com.qin.miaosha.redis.MiaoshaKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.service.MiaoshaService;
import com.qin.miaosha.service.OrderService;
import com.qin.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("miaoshaService")
public class MiaoshaServiceImpl implements MiaoshaService {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisService redisService;

    @Transactional
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goodsVo){
        //生成订单
        OrderInfo orderInfo=orderService.createOrder(user,goodsVo);
        //减少库存
        boolean success =goodsService.reduceStock(goodsVo);
        if(!success){
            setGoodsOver(goodsVo.getId());
        }

        return  orderInfo;

    }

    public long getMiaoshaResult(long userId,long goodsId){
        MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
        if(order!=null){
            //秒杀成功
            return order.getOrderId();
        }else{
            if(getGoodsOver(goodsId)){
                return -1;
            }
            return 0;
        }
    }

    private void setGoodsOver(Long goodsId) {
        redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
    }

    private boolean getGoodsOver(long goodsId) {
        return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId).getData();
    }
}
