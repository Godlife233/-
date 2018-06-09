package com.qin.miaosha.service;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.vo.GoodsVo;

import java.util.List;

public interface GoodsService {
    public ServerResponse<List<GoodsVo>> listGoodsVo();

    public ServerResponse<GoodsVo> getById(long id);

    public boolean reduceStock(GoodsVo goodsVo);
}
