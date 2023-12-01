package com.yanzu.module.member.service.iot;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanzu.module.member.service.iot.ewlbean.EwlHandReqVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.net.ssl.*;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
public class MyWebSocketClient {

    @Value("${ewelink.appid}")
    private String appid;
    //    @Value("${ewelink.token:1}")
//    private String token;
    @Value("${ewelink.apiKey}")
    private String apiKey;
    @Resource
    private EwlService ewlService;
    private Timer heartbeatTimer;
    private Integer hbInterval = 60;
    private WebSocketClient webSocketClient;


    @SneakyThrows
    @PostConstruct
    public void init() {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.close();
        }
        String socketUrl = ewlService.getSocketUrl();
        webSocketClient = createWebSocketClient(socketUrl);
        webSocketClient.connectBlocking();
        //握手
        hand();
    }

    public WebSocketClient createWebSocketClient(String serverUrl) {
        try {
            // 忽略SSL证书验证
            TrustManager trustManager = new X509ExtendedTrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, Socket socket) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType, SSLEngine engine) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[]{};
                }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            URI serverURI = new URI(serverUrl);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            // 创建WebSocket客户端实例
            WebSocketClient webSocketClient = new WebSocketClient(serverURI) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
                    log.info("WebSocket【易微联】建立连接");
                }

                @Override
                public void onMessage(String s) {
                    log.info("WebSocket【易微联】收到来自服务端的消息：{}", s);
                    JSONObject data = JSONObject.parseObject(s);
                    Integer error = data.getInteger("error");
                    if (!ObjectUtils.isEmpty(error)) {
                        if (error == 0) {
                            //启动时 握手认证的回复
                            JSONObject config = data.getJSONObject("config");
                            if (!ObjectUtils.isEmpty(config)) {
                                if (!ObjectUtils.isEmpty(config.getInteger("hbInterval"))) {
                                    hbInterval = config.getInteger("hbInterval");
                                }
                                startHeartbeat();
                            }
                        }
                    }
                    //离线在线通知
                    String action = data.getString("action");
                    if (!ObjectUtils.isEmpty(action)) {
                        String deviceid = data.getString("deviceid");
                        JSONObject params = data.getJSONObject("params");
                        Boolean online = params.getBoolean("online");
                        log.info("WebSocket【易微联】设备:{},状态:{}", deviceid, online);
                    }

                }

                @Override
                public void onClose(int i, String s, boolean b) {
                    log.info("WebSocket【易微联】关闭连接");
                    log.info("WebSocket关闭连接:::" + "i = " + i + ":::s = " + s + ":::b = " + b);
                    if (!ObjectUtils.isEmpty(heartbeatTimer)) {
                        heartbeatTimer.cancel();
                    }
                }

                @Override
                public void onError(Exception e) {
                    log.info("WebSocket【易微联】报错了：{}", e.getMessage());
                }
            };
            webSocketClient.setSocketFactory(socketFactory);
            return webSocketClient;
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double generateRandomDouble(double min, double max) {
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    private void startHeartbeat() {
        heartbeatTimer = new Timer(true);
        String token = ewlService.getToken();
        int v = (int) (hbInterval * generateRandomDouble(0.7, 0.9));
        heartbeatTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                log.info("WebSocket【易微联】发送心跳数据包！心跳间隔:{}秒", v);
                EwlHandReqVO reqVO = new EwlHandReqVO();
                reqVO.setAppid(appid);
                reqVO.setApikey(apiKey);
                reqVO.setAt(token);
                sendToServer(JSON.toJSONString(reqVO));
            }
        }, 0, v * 1000);
    }

    public void hand() {
        String token = ewlService.getToken();
        EwlHandReqVO reqVO = new EwlHandReqVO();
        reqVO.setAppid(appid);
        reqVO.setApikey(apiKey);
        reqVO.setAt(token);
        webSocketClient.send(JSON.toJSONString(reqVO));
    }


    public void sendToServer(String body) {
        if (webSocketClient != null && webSocketClient.isOpen()) {
            webSocketClient.send(body);
            log.info("WebSocket【易微联】已发送消息到服务端：{}", body);
        } else {
            init();
            log.info("WebSocket【易微联】连接未打开");
        }
    }


}
