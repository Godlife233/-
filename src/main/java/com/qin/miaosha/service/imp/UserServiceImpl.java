package com.qin.miaosha.service.imp;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.UserDao;
import com.qin.miaosha.domain.User;
import com.qin.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired
    UserDao userDao;

    @Override
    public ServerResponse<User> getByid(int id) {
       return   ServerResponse.createBySuccess(userDao.getById(id));
    }
}
