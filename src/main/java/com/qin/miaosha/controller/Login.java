package com.qin.miaosha.controller;

import com.qin.miaosha.Utils.ValidatorUtil;
import com.qin.miaosha.common.ResponseCode;
import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.service.MiaoShaUserService;
import com.qin.miaosha.vo.LoginVo;


import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.bcel.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/login/")
public class Login {

    private static Logger logger = LoggerFactory.getLogger(Login.class);

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @RequestMapping("to_login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "do_login",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse doLogin(HttpServletResponse response,LoginVo loginVo){
        logger.info(loginVo.toString());



        //登录
       return miaoShaUserService.login(response,loginVo);




    }


}
