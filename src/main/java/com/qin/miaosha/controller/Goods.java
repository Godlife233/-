package com.qin.miaosha.controller;

import com.qin.miaosha.common.Const;
import com.qin.miaosha.common.ServerResponse;
import com.qin.miaosha.domain.MiaoShaUser;
import com.qin.miaosha.redis.GoodsKey;
import com.qin.miaosha.redis.KeyPrefix;
import com.qin.miaosha.redis.MiaoShaUserKey;
import com.qin.miaosha.redis.RedisService;
import com.qin.miaosha.service.GoodsService;
import com.qin.miaosha.service.MiaoShaUserService;
import com.qin.miaosha.vo.GoodsDetailVo;
import com.qin.miaosha.vo.GoodsVo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/goods/")
public class Goods {

    @Autowired
    RedisService redisService;

    @Autowired
    MiaoShaUserService miaoShaUserService;

    @Autowired
    GoodsService goodsService;

    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    ApplicationContext applicationContext;


    @RequestMapping(value = "to_list",produces = "text/html")
    @ResponseBody
    public String toList(HttpServletResponse response, HttpServletRequest request ,MiaoShaUser miaoShaUser, Model model){
        model.addAttribute("user",miaoShaUser);

        //取缓存
        ServerResponse serverResponse =  redisService.get(GoodsKey.getGoodsList,"",String.class);
        String html =(String)serverResponse.getData();
        if(!StringUtils.isEmpty(html)){
            return html;
        }

       List<GoodsVo> goods = goodsService.listGoodsVo().getData();
        model.addAttribute("goodsList",goods);


        SpringWebContext ctx = new SpringWebContext(request,response,request.getServletContext(),request.getLocale(),
                model.asMap(),applicationContext);
        //手动渲染
        html =thymeleafViewResolver.getTemplateEngine().process("goods_list",ctx);


        if(!StringUtils.isEmpty(html)){
            redisService.set(GoodsKey.getGoodsList,"",html);
        }

      return html;
    }

    @RequestMapping(value = "detail/{goodsId}")
    @ResponseBody
    public ServerResponse<GoodsDetailVo> todetail(MiaoShaUser miaoShaUser, Model model, @PathVariable("goodsId") long goodsId){

        GoodsVo goodsVo = goodsService.getById(goodsId).getData();

        long endTime=goodsVo.getEndDate().getTime();
        long  startTime=goodsVo.getStartDate().getTime();
        long now = System.currentTimeMillis();

        int miaoshaStatus = 0;
        int remainSeconds =0;

        if(now<startTime){
            //倒计时
            miaoshaStatus = 0;
            remainSeconds =(int) (startTime-now)/1000;
        }else if(now>endTime){
            //秒杀结束
            miaoshaStatus = 2;
            remainSeconds = -1;
        }else{
            //秒杀进行中
            remainSeconds = 0;
            miaoshaStatus = 1;
        }

        GoodsDetailVo  vo =new GoodsDetailVo();
        vo.setGoodsVo(goodsVo);
        vo.setMiaoshaStatus(miaoshaStatus);
        vo.setRemainSeconds(remainSeconds);
        vo.setUser(miaoShaUser);

        return ServerResponse.createBySuccess(vo);
    }



}
