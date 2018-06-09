package com.qin.miaosha.rabibitmq;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.service.MiaoshaService;
import com.qin.miaosha.service.OrderService;
import com.qin.miaosha.vo.GoodsVo;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQReceiver {
    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    RedisService redisService;

    @RabbitListener(queues = MQConfig.MIAOSHA_QUEUE)
    public void receive(String message){
       MiaoShaMessage msm = RedisService.stringToBean(message,MiaoShaMessage.class);
       long goodId =msm.getGoodId();
       MiaoShaUser user =msm.getMiaoShaUser();
        //判断库存
        GoodsVo good = goodsService.getById(goodId).getData();
        int stock =good.getGoodsStock();
        if(stock<=0){
            return;
        }

        //避免重复秒杀
        MiaoshaOrder miaoshaOrder=orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(),goodId);
        if(miaoshaOrder!=null){

            return;
        }

        //减库存，下订单
        miaoshaService.miaosha(user,good);

    }

}
