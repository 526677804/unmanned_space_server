//package com.yanzu.module.member.service.iot;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.yanzu.module.member.forest.EwlClient;
//import com.yanzu.module.member.service.iot.ewlbean.EwlHandReqVO;
//import lombok.extern.slf4j.Slf4j;
//import okhttp3.*;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.Resource;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@Slf4j
//public class MyOkHttpClient {
//
//    private String appid = "iSQwkQ8PfVRB75jaOgsPJtwfAdG1678f";
//    private String token = "28e3579792d46a0e00e323d0ced05d9e97691782";
//    private String apiKey = "f3bfc723-56e6-4f98-a69f-1a1ab94f3e87";
//    @Resource
//    private EwlClient ewlClient;
//
//    //    private OkHttpClient httpClient;
//    private WebSocket webSocket;
//
//    @PostConstruct
//    public void init() {
//        // 获取WebSocket连接地址
//        JSONObject socketUrl = ewlClient.getSocketUrl();
//        String serverUrl = "";
//        if (socketUrl != null && socketUrl.getInteger("error") == 0) {
//            serverUrl = String.format("wss://%s:%s/api/ws", socketUrl.getString("IP"), socketUrl.getString("port"));
//        } else {
//            //默认给一个地址
//            serverUrl = "wss://52.80.9.35:8080/api/ws";
//        }
//        connect(serverUrl);
//    }
//
//    public void connect(String serverUrl) {
//        // 创建OkHttpClient
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.pingInterval(30, TimeUnit.SECONDS);
//
//        Request request = new Request.Builder()
//                .url(serverUrl)
//                .build();
//        // 创建一个WebSocket连接
//        webSocket = builder.build().newWebSocket(request, new WebSocketListener() {
//            @Override
//            public void onOpen(WebSocket webSocket, Response response) {
//                //握手认证
//                EwlHandReqVO reqVO = new EwlHandReqVO();
//                reqVO.setAppid(appid);
//                reqVO.setApikey(apiKey);
//                reqVO.setAt(token);
//                webSocket.send(JSON.toJSONString(reqVO));
//                System.out.println(1);
//            }
//
//            @Override
//            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
//                // WebSocket连接失败后的处理逻辑
//                System.out.println("WebSocket Connection Failed");
//            }
//
//            @Override
//            public void onClosing(WebSocket webSocket, int code, String reason) {
//                // WebSocket连接关闭前的处理逻辑
//                System.out.println("WebSocket Closing");
//            }
//
//            @Override
//            public void onClosed(WebSocket webSocket, int code, String reason) {
//                // WebSocket连接关闭后的处理逻辑
//                System.out.println("WebSocket Closed");
//            }
//
//            @Override
//            public void onMessage(WebSocket webSocket, String text) {
//                // 收到服务器发送的消息的处理逻辑
//                System.out.println("Received Message: " + text);
//                // 在这里可以处理接收到的消息
//            }
//        });
//
//    }
//
//
////    private void performHandshake(String serverUrl) {
////        EwlHandReqVO reqVO = new EwlHandReqVO();
////        reqVO.setAppid(appid);
////        reqVO.setApikey(apiKey);
////        reqVO.setAt(token);
////        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(reqVO));
////        Request request = new Request.Builder()
////                .url(serverUrl)
////                .post(requestBody)
////                .build();
////
////        try (Response response = okHttpClient.newCall(request).execute()) {
////            if (response.isSuccessful()) {
////                // 握手成功，可以处理返回的数据
////                JSONObject json = JSONObject.parseObject(response.body().string());
////                System.out.println(json);
////            } else {
////                // 握手失败，可以处理失败情况
////                System.err.println("Handshake failed. Code: " + response.code());
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
//
//
//}
