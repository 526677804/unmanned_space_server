package com.yanzu.module.member.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MqttProviderCallBack implements MqttCallback {

    @Value("${mqtt.client.id}")
    private String clientId;

    /**
     * 与服务器断开的回调
     */
    @Override
    public void connectionLost(Throwable cause) {
        log.error("{}与mqtt服务器断开连接", clientId);
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        IMqttAsyncClient client = token.getClient();
        log.info("{}发布消息成功", client.getClientId());
    }
}
