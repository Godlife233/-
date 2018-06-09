package com.qin.miaosha.service;

import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.domain.MiaoshaOrder;
import com.qin.miaosha.domain.OrderInfo;
import com.qin.miaosha.vo.GoodsVo;

public interface MiaoshaService {
    public OrderInfo miaosha(MiaoShaUser user, GoodsVo goodsVo);
    public long getMiaoshaResult(long userId,long goodsId);
}
