package com.qin.miaosha.service.imp;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.GoodsDao;
import com.qin.miaosha.dao.OrderDao;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;
import com.qin.miaosha.domain.OrderInfo;
import com.qin.miaosha.redis.OrderKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.OrderService;
import com.qin.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service("orderService")
public class OrderServiceImpl implements OrderService{
    @Autowired
    OrderDao orderDao;

    @Autowired
    RedisService redisService;
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId,long goodsId){
      //  return orderDao.getMiaoshaOrderByUserIdGoodsId(userId,goodsId);
         ServerResponse serverResponse =redisService.get(OrderKey.getMiaoshaOrderByUidGid,""+userId+"_"+goodsId,MiaoshaOrder.class);
         if(!serverResponse.isSuccess()){
             return  null ;

         }
         else return  (MiaoshaOrder) serverResponse.getData();
    }



    @Transactional
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCreateDate(new Date());
        orderInfo.setDeliveryAddrId(0L);
        orderInfo.setGoodsCount(1);
        orderInfo.setGoodsId(goods.getId());
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getMiaoshaPrice());
        orderInfo.setOrderChannel(1);
        orderInfo.setStatus(0);
        orderInfo.setUserId(user.getId());
        orderDao.insert(orderInfo);
        MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
        miaoshaOrder.setGoodsId(goods.getId());
        miaoshaOrder.setOrderId(orderInfo.getId());
        miaoshaOrder.setUserId(user.getId());
        orderDao.insertMiaoshaOrder(miaoshaOrder);

        redisService.set(OrderKey.getMiaoshaOrderByUidGid,""+user.getId()+"_"+goods.getId(),miaoshaOrder);

        return orderInfo;
    }
    public ServerResponse<OrderInfo>getOrderById(long id){
        OrderInfo order = orderDao.getOrderById(id);
        if(order==null){
            return ServerResponse.createByErrorMessage("订单不存在");
        }
        else
            return ServerResponse.createBySuccess(order);

    }
}
