package com.yonyou.einvoice.demo.config;

import com.yonyou.einvoice.demo.converter.FastJsonMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@Slf4j
public class RabbitmqConfig {

  @Autowired
  private CachingConnectionFactory connectionFactory;

  /**
   * 单一消费者
   *
   * @return
   */
  @Bean(name = "singleListenerContainer")
  public SimpleRabbitListenerContainerFactory listenerContainer() {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(new FastJsonMessageConverter());
    factory.setConcurrentConsumers(1);
    factory.setMaxConcurrentConsumers(1);
    factory.setPrefetchCount(1);
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    return factory;
  }

  @Bean
  public RabbitTemplate rabbitTemplate() {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(new FastJsonMessageConverter());
    /**
     * 消息发送失败返回到队列中，需要配置publisher-returns=true
     * 当mandatory标志位设置为true时
     * 如果exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息
     * 那么broker会调用basic.return方法将消息返还给生产者
     * 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃
     */
    rabbitTemplate.setMandatory(true);
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
      if(ack){
        log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
      } else {
        log.info("消息发送失败:correlationData({}),ack({}),cause({})", correlationData, ack, cause);
      }

    });
    rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log
        .info("消息发送失败:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange,
            routingKey, replyCode, replyText, message));
    return rabbitTemplate;
  }


}
