package com.yanzu.module.member.service.iot;

import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.forest.EwlClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.Resource;
import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2021/6/26.
 */
@Configuration
@Order(1)
@Slf4j
public class WebSocketConfig implements ApplicationRunner {


    private String appid = "iSQwkQ8PfVRB75jaOgsPJtwfAdG1678f";
    private String token = "28e3579792d46a0e00e323d0ced05d9e97691782";
    private String apiKey = "f3bfc723-56e6-4f98-a69f-1a1ab94f3e87";
    @Resource
    private EwlClient ewlClient;
    private String serverUrl;
    private static Boolean isOk;

    private static WebSocketContainer container = ContainerProvider.getWebSocketContainer();

    private WebSocketClient client;

    /**
     * 定义定时任务线程
     */
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);


    /**
     * <run>
     *
     * @param args args
     * @throws
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("[WebSocketConfig] web socket init start.");
        // websocket客户端初始化
        wsClientInit();
    }

    /**
     * <websocket客户端初始化>
     *
     * @throws
     */
    public void wsClientInit() {
        log.info("[WebSocketConfig] start to wsClientInit");
        // 获取WebSocket连接地址
        JSONObject socketUrl = ewlClient.getSocketUrl();
        if (socketUrl != null && socketUrl.getInteger("error") == 0) {
            serverUrl = String.format("wss://%s:%s/api/ws", socketUrl.getString("IP"), socketUrl.getString("port"));
        } else {
            //默认给一个地址
            serverUrl = "wss://52.80.9.35:8080/api/ws";
        }
        try {
            client = new WebSocketClient();
            WebSocketClient.beforeInit();
            container.connectToServer(client, new URI(serverUrl));
            isOk = true;
        } catch (Exception e) {
            isOk = false;
            log.error("error, cause: ", e);
        }

        /**
         *  参数：1、任务体
         *        2、首次执行的延时时间
         *        3、任务执行间隔
         *        4、间隔时间单位
         **/
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //心跳检测 断线重连
                heartbeatCheck();
            }
        }, 1, 30, TimeUnit.SECONDS);

        log.info("[WebSocketConfig] end to wsClientInit");
    }

    /**
     * <心跳检测 断线重连>
     *
     * @throws
     */
    private void heartbeatCheck() {
        log.info("[WebSocketConfig] start to heartbeatCheck");
        if (isOk != null && isOk) {
            try {
                client.send("ping ");
            } catch (Exception e) {
                isOk = false;
            }
        } else {
            // 系统连接失败进行重试
            log.warn("系统连接失败，正在重连...");
            try {
                client.send("ping ");
                log.warn("系统重连成功！");
                isOk = true;
            } catch (Exception e) {
                try {
                    client = new WebSocketClient();
                    container.connectToServer(client, new URI(serverUrl));
                    isOk = true;
                } catch (Exception e1) {
                    isOk = false;
                }

                if (isOk != null && isOk) {
                    log.warn("系统重连成功！");
                }
            }
        }
    }
}