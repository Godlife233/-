package com.qin.miaosha.rabibitmq;

import com.qin.miaosha.redis.RedisService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MQSender {
    @Autowired
    AmqpTemplate amqpTemplate;

    public void sendMiaoShaMessage(MiaoShaMessage message){
        String msg = RedisService.beantoString(message);
        amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE,msg);
    }
}
