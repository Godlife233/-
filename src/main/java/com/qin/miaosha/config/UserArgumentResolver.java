package com.qin.miaosha.config;

import com.qin.miaosha.common.Const;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.service.MiaoShaUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        Class<?> clazz = methodParameter.getParameterType();
        return clazz== MiaoShaUser.class;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request =nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response =nativeWebRequest.getNativeResponse(HttpServletResponse.class);

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
