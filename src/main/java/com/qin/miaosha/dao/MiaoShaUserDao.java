package com.qin.miaosha.dao;

import com.qin.miaosha.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MiaoShaUserDao {
    @Select("select * from miaosha_user where id=#{id}")
    public MiaoShaUser getById(@Param("id") long id);

    @Select("select * from miaosha_user where id=#{id} and password=#{password} ")
    public MiaoShaUser login(@Param("id")long id,@Param("password")String password);
}
