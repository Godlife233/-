package com.qin.miaosha.service.imp;

import com.qin.miaosha.Utils.MD5Util;
import com.qin.miaosha.Utils.UUIDUtil;
import com.qin.miaosha.Utils.ValidatorUtil;
import com.qin.miaosha.common.Const;
import com.qin.miaosha.common.ResponseCode;
import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.MiaoShaUserDao;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.redis.MiaoShaUserKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.MiaoShaUserService;
import com.qin.miaosha.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Service("miaoShaUserService")
public class MiaoShaUserServiceImpl implements MiaoShaUserService {

    @Autowired
    MiaoShaUserDao miaoShaUserDao;

    @Autowired
    RedisService redisService;

    @Override
    public ServerResponse<MiaoShaUser> getById(long id) {

        //取缓存
        MiaoShaUser m =(MiaoShaUser) redisService.get(MiaoShaUserKey.getById,""+id,MiaoShaUser.class).getData();
        if(m!=null){
            return ServerResponse.createBySuccess(m);
        }


       //取数据库
       m= miaoShaUserDao.getById(id);
       if(m==null){
            return ServerResponse.createByErrorMessage("User不存在");
       }
       //存缓存
        redisService.set(MiaoShaUserKey.getById,""+id,m);
        return  ServerResponse.createBySuccess(m);
    }

    @Override
    public ServerResponse<MiaoShaUser> login(HttpServletResponse response,@Valid LoginVo loginVo) {



        long id = Long.parseLong(loginVo.getMobile());
        String password = loginVo.getPassword();

        if(StringUtils.isEmpty(password)||StringUtils.isEmpty(loginVo.getMobile()))
            return ServerResponse.createByErrorMessage("请输入手机号或密码");

        if(!ValidatorUtil.isMobile(loginVo.getMobile())){
            ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),"手机号格式错误");
        }



        MiaoShaUser m =miaoShaUserDao.getById(id);
        if(m==null){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //验证密码;

        //String dbSalt = m.getSalt();

        //String calcPass= MD5Util.formPassToDBPass(password,dbSalt);

        MiaoShaUser m2=miaoShaUserDao.login(id,password);
        if(m2!=null){
            String token = UUIDUtil.uuid();
            //生成cookie
            addCokie(m2,token,response);

            return ServerResponse.createBySuccess(m2);
        }else {
            return ServerResponse.createByErrorMessage("密码错误");
        }
    }

    public MiaoShaUser getBytoken(String token,HttpServletResponse response){
        ServerResponse serverResponse= redisService.get(MiaoShaUserKey.token,token, MiaoShaUser.class);
        if(serverResponse.isSuccess()){
            MiaoShaUser m =(MiaoShaUser)serverResponse.getData();
            //延长有效期
            addCokie(m,token,response);
            return m;
        }
        else {
            return null ;
        }
    }

    private void addCokie(MiaoShaUser user,String token,HttpServletResponse response){

        redisService.set(MiaoShaUserKey.token,token,user);
        Cookie cookie = new Cookie(Const.COOKI_NAME_TOKEN,token);
        cookie.setMaxAge(Const.COOKI_SECOND);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
