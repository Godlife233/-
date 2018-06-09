package com.qin.miaosha.controller;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.OrderInfo;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.service.OrderService;
import com.qin.miaosha.vo.GoodsVo;
import com.qin.miaosha.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/order/")
public class OrderController {
    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("detail")
    @ResponseBody
    public ServerResponse<OrderDetailVo> detail(Model model, MiaoShaUser user, @RequestParam("orderId") long orderId){
        if(user==null){
            return ServerResponse.createByErrorMessage("请登录");

        }
        ServerResponse serverResponse =orderService.getOrderById(orderId);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }

        OrderInfo order =(OrderInfo) serverResponse.getData();
        long goodsId =order.getGoodsId();
        serverResponse = goodsService.getById(goodsId);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        GoodsVo goodsVo = (GoodsVo) serverResponse.getData();
        OrderDetailVo orderVo = new OrderDetailVo();
        orderVo.setGoods(goodsVo);
        orderVo.setOrder(order);
        return ServerResponse.createBySuccess(orderVo);


    }
}
