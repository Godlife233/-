package com.qin.miaosha.service;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;
import com.qin.miaosha.domain.OrderInfo;
import com.qin.miaosha.vo.GoodsVo;

public interface OrderService {
    public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId);
    public OrderInfo createOrder(MiaoShaUser user, GoodsVo goods);
    public ServerResponse<OrderInfo> getOrderById(long id);
}
