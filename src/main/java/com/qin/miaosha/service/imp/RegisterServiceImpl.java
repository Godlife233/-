package com.qin.miaosha.service.imp;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.MiaoShaUserDao;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.service.MailService;
import com.qin.miaosha.service.RegisterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("registerService")
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    MiaoShaUserDao miaoShaUserDao;
    @Autowired
    MailService mailService;
    @Override
    public ServerResponse<String> register(MiaoShaUser user) {
        if(miaoShaUserDao.register(user)>0){
            mailService.sendEmail(user);
            return ServerResponse.createBySuccess("注册成功,请前往您的邮箱激活账户");
        }
        else return ServerResponse.createByErrorMessage("注册失败，请检查您的账号是否重复");
    }
}
