package com.qin.miaosha.controller;

import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.dao.UserDao;
import com.qin.miaosha.domain.User;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.redis.UserKey;
import com.qin.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo/")
public class demo {

    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;

    @RequestMapping("thymeleaf")
    public String thymeleaf(Model model){
        model.addAttribute("name","Tom");
        return "hello";
    }

    @RequestMapping(value = "db/get",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> dbget(int id){
       return userService.getByid(id);

    }

    @RequestMapping(value = "redis/get",method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse redisGet(String key){
        return redisService.get(UserKey.getById,key,Integer.class);

    }

    @RequestMapping(value = "redis/set",method = RequestMethod.GET)
    @ResponseBody
    public <T>ServerResponse redisSet(String key,Integer value){
        return redisService.set(UserKey.getById,key,value);

    }

    @RequestMapping(value = "redis/incr",method = RequestMethod.GET)
    @ResponseBody
    public <Long>ServerResponse redisIncr(String key){
        return redisService.incr(UserKey.getById,key);

    }

    @RequestMapping(value = "redis/decr",method = RequestMethod.GET)
    @ResponseBody
    public <Long>ServerResponse redisSet(String key){
        return redisService.decr(UserKey.getById,key);

    }

    @RequestMapping(value = "redis/exists",method = RequestMethod.GET)
    @ResponseBody
    public <Boolean>ServerResponse redisExists(String key){
        return redisService.exists(UserKey.getById,key);

    }
}
