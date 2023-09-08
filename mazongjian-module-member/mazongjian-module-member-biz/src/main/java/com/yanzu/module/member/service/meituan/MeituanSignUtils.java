package com.yanzu.module.member.service.meituan;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MeituanSignUtils {

    public static String generateSign(Map<String, String> params, String appSecret, String signMethod) {
        //特殊处理 移除sign字段
        params.remove("sign");
        // 第一步：参数排序
        List<String> keys = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isNotEmpty(entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        Collections.sort(keys);
        // 第二步：把所有参数名和参数值串在一起
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(appSecret)) {
            sb.append(appSecret);
        }
        for (String key : keys) {
            sb.append(key).append(params.get(key).trim());
        }
        if (StringUtils.isNotEmpty(appSecret)) {
            sb.append(appSecret);
        }
        String encryptionKey = sb.toString().trim();
        // 第三步：加签
        if (signMethod.equals("MD5")) {
            try {
                String sign = genMd5(encryptionKey);
                return sign;
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            //开发者暂不需支持，支持MD5即可
            return "";
        }
    }

    public static String genMd5(String info) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] infoBytes = info.getBytes();
        md5.update(infoBytes);
        byte[] sign = md5.digest();
        return byteArrayToHex(sign);
    }

    public static String byteArrayToHex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toLowerCase());
        }
        return sign.toString();
    }

    public static Map<String, String> convertBeanToMap(Object bean)  {
        // 创建空的Map对象
        Map<String, String> map = new HashMap<>();
        // 获取当前对象的类
        Class<?> clazz = bean.getClass();
        // 遍历类层级结构，包括父类的字段
        while (clazz != null) {
            // 获取当前类的所有字段
            Field[] fields = clazz.getDeclaredFields();
            // 遍历字段列表，将字段名作为键，字段值作为值，添加到Map中
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = null;
                try {
                    fieldValue = field.get(bean);
                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
                }
                map.put(fieldName, String.valueOf(fieldValue));
            }
            // 获取父类
            clazz = clazz.getSuperclass();
        }
        return map;
    }
}