package com.yanzu.module.member.service.iot.iotbean;

import lombok.Data;

import java.util.Random;

@Data
public class IotApiV2RegDoorDataReqVO {


    private String admin_pwd = "0" + generateRandomNumericString(4);//激活码，自定义5位数字在前一位补0


    private String device_cid = generateRandomNumericString(20);//cid，自定义20位数字


    private static String generateRandomNumericString(int length) {
        // 设置随机数生成范围
        int min = 0;
        int max = 9;

        // 创建 Random 对象
        Random random = new Random();

        // 生成指定长度的全数字字符串
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt((max - min) + 1) + min;
            stringBuilder.append(digit);
        }

        return stringBuilder.toString();
    }


}
