package com.message;

import com.config.RabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RpcReceiveServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcReceiveServer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitConfig.RPC_QUEUE1)
    public void process(Message msg) {
        logger.info("server receive : {}",msg.toString());
        logger.info("收到消息：{}",new String(msg.getBody()));
        Message response = MessageBuilder.withBody(("i'm receive:"+new String(msg.getBody())).getBytes()).build();
        CorrelationData correlationData = new CorrelationData(msg.getMessageProperties().getCorrelationId());
        rabbitTemplate.sendAndReceive(RabbitConfig.RPC_EXCHANGE, RabbitConfig.RPC_QUEUE2, response, correlationData);
    }
}
