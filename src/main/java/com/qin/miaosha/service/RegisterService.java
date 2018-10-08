package com.qin.miaosha.service;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;



public interface RegisterService {
    public ServerResponse<String> register(MiaoShaUser user);
}
