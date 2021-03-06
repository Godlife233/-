package com.qin.miaosha.dao;

import com.qin.miaosha.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserDao {

    @Select("select id,name from user where id =#{id}")
   public User getById(@Param("id") int id);
}
