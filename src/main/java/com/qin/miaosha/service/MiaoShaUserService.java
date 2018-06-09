package com.qin.miaosha.service;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.vo.LoginVo;

import javax.servlet.http.HttpServletResponse;


public interface MiaoShaUserService {

    public ServerResponse<MiaoShaUser> getById(long id);

    public ServerResponse<MiaoShaUser> login(HttpServletResponse response,LoginVo loginVo);

    public MiaoShaUser getBytoken(String token,HttpServletResponse response);

}
