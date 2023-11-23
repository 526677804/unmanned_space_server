package com.yanzu.module.member.service.iot;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.*;
import java.io.IOException;

/**
 * Created by user on 2021/6/26.
 */
@ClientEndpoint
@Slf4j
public class WebSocketClient {


    /**
     * session
     */
    private Session session;

    /**
     * <beforeInit>
     *
     * @throws
     */
    public static void beforeInit() {
        // 在socket配置类中调用此方法可以完成一些需要初始化注入的操作
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("连接开启...");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // redisUtils.set(UUID.randomUUID().toString().replaceAll("-", ""), message);
        log.info(message);
    }

    @OnClose
    public void onClose() {
        log.info("长连接关闭...");
    }

    @OnError
    public void onError(Session session, Throwable t) {
        log.error("error, cause: ", t);
    }

    /**
     * <异步发送message>
     *
     * @param message message
     * @throws
     */
    public void send(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    /**
     * <发送message>
     *
     * @param message message
     * @throws
     */
    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException ex) {
            log.error("error, cause: ", ex);
        }
    }

    /**
     * <关闭连接>
     *
     * @throws
     */
    public void close() throws IOException {
        if (this.session.isOpen()) {
            this.session.close();
        }
    }
}
