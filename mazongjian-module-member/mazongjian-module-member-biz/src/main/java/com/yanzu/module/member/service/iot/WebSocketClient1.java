//package com.yanzu.module.member.service.iot;
//
//
//import com.alibaba.fastjson.JSONObject;
//import com.yanzu.module.member.forest.EwlClient;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//import javax.net.ssl.X509TrustManager;
//import java.security.GeneralSecurityException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class WebSocketClient1 {
//
//    private OkHttpClient httpClient;
//    private WebSocket webSocket;
//
//    @Resource
//    private EwlClient ewlClient;
//
//    @PostConstruct
//    public void init() {
//        // 获取WebSocket连接地址
//        JSONObject socketUrl = ewlClient.getSocketUrl();
//        if (socketUrl != null && socketUrl.getInteger("error") == 0) {
//            String url = String.format("wss://{}:{}/api/ws", socketUrl.getString("IP"), socketUrl.getString("port"));
//            log.info("易微联websockcet地址:{}", url);
//            connect(url);
//        }
//    }
//
//    public void connect(String url) {
//        try {
//            // 创建自定义的TrustManager，用于跳过SSL证书校验
//            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
//                @Override
//                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//                @Override
//                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//                }
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return new X509Certificate[0];
//                }
//            }};
//
//            // 创建SSL上下文
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, trustAllCerts, null);
//            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
//
//            // 创建OkHttpClient
//            OkHttpClient.Builder builder = new OkHttpClient.Builder();
//            builder.pingInterval(10, TimeUnit.SECONDS);
//            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
//            builder.hostnameVerifier((hostname, session) -> true);
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .build();
//
//            // 创建一个WebSocket连接
//            webSocket = builder.build().newWebSocket(request, new WebSocketListener() {
//
//                @Override
//                public void onOpen(WebSocket webSocket, Response response) {
//                    // WebSocket连接成功后的处理逻辑
//                    System.out.println("WebSocket Connected");
//
//                    // 在认证成功后发送心跳数据
//                    authenticate();
//                }
//
//                @Override
//                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                    // WebSocket连接失败后的处理逻辑
//                    System.out.println("WebSocket Connection Failed");
//                }
//
//                @Override
//                public void onClosing(WebSocket webSocket, int code, String reason) {
//                    // WebSocket连接关闭前的处理逻辑
//                    System.out.println("WebSocket Closing");
//                }
//
//                @Override
//                public void onClosed(WebSocket webSocket, int code, String reason) {
//                    // WebSocket连接关闭后的处理逻辑
//                    System.out.println("WebSocket Closed");
//                }
//
//                @Override
//                public void onMessage(WebSocket webSocket, String text) {
//                    // 收到服务器发送的消息的处理逻辑
//                    System.out.println("Received Message: " + text);
//
//                    // 在这里可以处理接收到的消息
//                }
//            });
//        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
//        }
//    }
//    private void authenticate() {
//        // 在这里进行认证逻辑
//
//        // 认证成功后发送心跳数据
//        sendHeartbeat();
//    }
//
//    public void sendHeartbeat() {
//        if (webSocket != null && !StringUtils.isEmpty(HEARTBEAT_MSG)) {
//            webSocket.send(HEARTBEAT_MSG);
//        }
//    }
//
//    public void close() {
//        if (webSocket != null) {
//            webSocket.close(1000, "Client Close");
//        }
//    }
//}