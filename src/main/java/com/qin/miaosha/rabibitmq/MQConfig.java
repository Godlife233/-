package com.qin.miaosha.rabibitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class MQConfig {

    public static final  String QUEUE="queue";
    public static final  String MIAOSHA_QUEUE="miaosha.queue";



    @Bean
    public Queue queque(){
        return new Queue(MIAOSHA_QUEUE,true);

    }



}
