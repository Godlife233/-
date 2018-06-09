package com.qin.miaosha.service;


import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.User;

public interface UserService {

    public ServerResponse<User> getByid(int id);
}
