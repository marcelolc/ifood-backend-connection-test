package com.cyreno;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeepAliveSender {

    private final String brokerUrl;
    private final String topic;
    private final int qos;

    public KeepAliveSender(
            @Value("${keepalive.receiver.brokerUrl}") String brokerUrl,
            @Value("${keepalive.receiver.topic}") String topic,
            @Value("${keepalive.receiver.qos}") int qos) {
        this.brokerUrl = brokerUrl;
        this.topic = topic;
        this.qos = qos;
    }

    public void sendMsg(String mqttClientId, byte[] bytes) {
        try {

            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient mqttClient = new MqttClient(brokerUrl, mqttClientId, persistence);

            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setCleanSession(true);

            mqttClient.connect(connectOptions);

            MqttMessage message = new MqttMessage(bytes);
            message.setQos(qos);

            mqttClient.publish(topic, message);
            mqttClient.disconnect();

        } catch (MqttException me) {
            handleException(me);
        }
    }

    private void handleException(MqttException me) {
        System.out.println("reason " + me.getReasonCode());
        System.out.println("msg " + me.getMessage());
        System.out.println("loc " + me.getLocalizedMessage());
        System.out.println("cause " + me.getCause());
        System.out.println("excep " + me);
        me.printStackTrace();
    }
}
