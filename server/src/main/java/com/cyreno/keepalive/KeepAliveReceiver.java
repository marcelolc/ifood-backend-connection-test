package com.cyreno.keepalive;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.UUID;

/**
 * KeepAliveReceiver
 * https://docs.spring.io/spring-integration/reference/html/mqtt.html
 */
@Configuration
public class KeepAliveReceiver {

    private final KeepAliveHandler keepAliveHandler;
    private final String brokerUrl;
    private final String topic;
    private final int qos;

    public KeepAliveReceiver(KeepAliveHandler keepAliveHandler,
                             @Value("${keepalive.receiver.brokerUrl}") String brokerUrl,
                             @Value("${keepalive.receiver.topic}") String topic,
                             @Value("${keepalive.receiver.qos}") int qos) {
        this.keepAliveHandler = keepAliveHandler;
        this.brokerUrl = brokerUrl;
        this.topic = topic;
        this.qos = qos;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {

        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(brokerUrl, UUID.randomUUID().toString(), topic);
        adapter.setCompletionTimeout(5000);

        DefaultPahoMessageConverter messageConverter = new DefaultPahoMessageConverter();
        messageConverter.setPayloadAsBytes(true);
        adapter.setConverter(messageConverter);

        adapter.setQos(qos);
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;

    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return this::handleMessage;
    }

    private void handleMessage(Message<?> message) {
        KeepAliveMessage keepAliveMessage = KeepAliveMessage.fromBytes((byte[]) message.getPayload());
        keepAliveHandler.handleMessage(keepAliveMessage);
    }

}