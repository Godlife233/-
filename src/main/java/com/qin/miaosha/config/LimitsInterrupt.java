package com.qin.miaosha.config;

import com.alibaba.fastjson.JSON;
import com.qin.miaosha.common.Const;
import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.redis.AccessKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class LimitsInterrupt extends HandlerInterceptorAdapter {
    @Autowired
    RedisService redisService ;

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){

            HandlerMethod hm = (HandlerMethod)handler;
            Limits limits = hm.getMethodAnnotation(Limits.class);
            if(limits == null){
               return true;
            }

            MiaoShaUser miaoShaUser  =    getUser(request,response);
            UserContext.setUser(miaoShaUser);
            int maxCount = limits.maxCount();
            int seconds = limits.seconds();
            boolean needLogin = limits.needLogin();
            String key = request.getRequestURI();
            if(needLogin){
                if(miaoShaUser==null){
                    render(response,ServerResponse.createByErrorMessage("请登录"));
                    return  false;
                }
                key +="-"+miaoShaUser.getId();
            }else {
                //do nothing
            }
            AccessKey ak =AccessKey.withExpire(seconds);
            Integer count = (Integer) redisService.get(ak,key,Integer.class).getData();
            if(count==null){
                redisService.set(ak,key,1);
            }else if(count<maxCount){
                redisService.incr(ak,key);
            }else {
                render(response,ServerResponse.createByErrorMessage("访问达到上限请稍后再访问"));
                return false;
            }




        }


        return super.preHandle(request, response, handler);
    }


    private void render(HttpServletResponse response,ServerResponse sr) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(sr);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }


    private MiaoShaUser getUser(HttpServletRequest request, HttpServletResponse response){


        //参数中的token
        String pToken = request.getParameter(Const.COOKI_NAME_TOKEN);

        //cookie中的token
        String cToken =getCookieValue(request,Const.COOKI_NAME_TOKEN);
        if(StringUtils.isEmpty(pToken)&&StringUtils.isEmpty(cToken)){
            return  null;
        }

        String token =StringUtils.isEmpty(pToken)?cToken:pToken;

        return miaoShaUserService.getBytoken(token,response);
    }

    private String getCookieValue(HttpServletRequest request,String cookieName){
        Cookie[] cookies=request.getCookies();
        for(Cookie cookie:cookies){
            if(cookie.getName().equals(cookieName)){
                return cookie.getValue();
            }
        }
        return  null;
    }
}
