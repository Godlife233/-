package com.qin.miaosha.controller;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.config.Limits;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;

import com.qin.miaosha.rabibitmq.MQSender;
import com.qin.miaosha.rabibitmq.MiaoShaMessage;
import com.qin.miaosha.redis.GoodsKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.service.MiaoshaService;
import com.qin.miaosha.service.OrderService;
import com.qin.miaosha.vo.GoodsVo;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/miaosha/")
public  class MiaoshaController implements InitializingBean  {

    @Autowired
    GoodsService goodsService;

    @Autowired
    OrderService orderService;

    @Autowired
    MiaoshaService miaoshaService;
    @Autowired
    RedisService redisService;
    @Autowired
    MQSender mqSender;


    private Map<Long,Boolean> localOverMap = new HashMap<Long, Boolean>();

    /**
     *
     * 系统初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
       List<GoodsVo> goodsList = goodsService.listGoodsVo().getData();
       if(goodsList==null){
           return ;
       }
       for(GoodsVo goodsVo:goodsList){
           redisService.set(GoodsKey.getMiaoShaGoodsStock,""+goodsVo.getId(),goodsVo.getStockCount());
           localOverMap.put(goodsVo.getId(),false);
       }
    }

    @Limits(maxCount = 5,seconds = 60,needLogin = true)
    @RequestMapping(value = "do_miaosha",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse doMiaosha(MiaoShaUser miaoShaUser, Model model,@RequestParam("goodsId") long goodsId){
        model.addAttribute("user",miaoShaUser);


        Boolean result =localOverMap.get(goodsId);
        if(result){
            return ServerResponse.createByErrorMessage("秒杀失败库存不足");
        }


        //避免重复秒杀
        MiaoshaOrder miaoshaOrder=orderService.getMiaoshaOrderByUserIdGoodsId(miaoShaUser.getId(),goodsId);
        if(miaoshaOrder!=null){

            return ServerResponse.createByErrorMessage("不能重复秒杀");
        }

        //预减库存
        long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock,""+goodsId).getData();
        if(stock<0){
            localOverMap.put(goodsId,true);
            return ServerResponse.createByErrorMessage("秒杀失败库存不足");

        }



        //入队
        MiaoShaMessage msm = new MiaoShaMessage();
        msm.setGoodId(goodsId);
        msm.setMiaoShaUser(miaoShaUser);
        mqSender.sendMiaoShaMessage(msm);



        //系统初始化时加载缓存


        /*
        //判断库存
       GoodsVo good = goodsService.getById(goodsId).getData();
        int stock =good.getGoodsStock();
        if(stock<=0){
             ServerResponse.createByErrorMessage("商品库存不足");

        }
        //避免重复秒杀
        MiaoshaOrder miaoshaOrder=orderService.getMiaoshaOrderByUserIdGoodsId(miaoShaUser.getId(),goodsId);
        if(miaoshaOrder!=null){

            return ServerResponse.createByErrorMessage("不能重复秒杀");
        }

        //减库存 下订单 写入秒杀订单
        OrderInfo orderInfo=miaoshaService.miaosha(miaoShaUser,good);



        return ServerResponse.createBySuccess(orderInfo);
        */
        return  ServerResponse.createBySuccess();
    }

    @Limits(maxCount = 5,seconds = 60,needLogin = true)
    @RequestMapping(value = "result",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Long> result(MiaoShaUser miaoShaUser, Model model,@RequestParam("goodsId") long goodsId) {
        model.addAttribute("user", miaoShaUser);

        long result = miaoshaService.getMiaoshaResult(miaoShaUser.getId(),goodsId);
        return ServerResponse.createBySuccess(result);
    }



}
