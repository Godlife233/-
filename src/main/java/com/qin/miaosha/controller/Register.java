package com.qin.miaosha.controller;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.service.MailService;
import com.qin.miaosha.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/register/")
public class Register {
    @Autowired
    MailService mailService;

    @Autowired
    RegisterService registerService;
    @RequestMapping("do_register")
    @ResponseBody
    public ServerResponse<String> register(MiaoShaUser user){
        registerService.register(user);
    }
    @RequestMapping("test")
    @ResponseBody
    public ServerResponse<String> testRegister(String to,String subject,Long id){
        return mailService.sendEmail(to,subject,id);
    }
}
