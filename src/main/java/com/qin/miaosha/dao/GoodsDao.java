package com.qin.miaosha.dao;

import com.qin.miaosha.domain.Goods;
import com.qin.miaosha.vo.GoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface GoodsDao {

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
    public List<GoodsVo> getGoodsVoList();

    @Select("select g.*,mg.stock_count,mg.start_date,mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id =#{id}")
    public GoodsVo getById(@Param("id") long id);

    @Update("update miaosha_goods set stock_count =stock_count-1 where goods_id =#{id} and stock_count>0 ")
    public int reduceStock(@Param("id")long goodsId);

}
