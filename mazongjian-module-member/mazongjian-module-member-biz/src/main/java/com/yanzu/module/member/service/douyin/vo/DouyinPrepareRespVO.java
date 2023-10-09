package com.yanzu.module.member.service.douyin.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/9 11:45
 */
@Data
public class DouyinPrepareRespVO {
    //用户实际支付价格  单位 分
    private Integer payAmount;
    //团购名称
    private String title;
    //一次验券的标识,在验券接口传入
    private String verifyToken;

    //加密券码, 在验券接口传入
    private String encryptedCode;
}
