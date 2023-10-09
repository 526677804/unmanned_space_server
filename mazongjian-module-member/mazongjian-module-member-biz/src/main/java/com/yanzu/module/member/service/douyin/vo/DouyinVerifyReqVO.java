package com.yanzu.module.member.service.douyin.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/9 12:24
 */
@Data
public class DouyinVerifyReqVO {

    //一次验券的标识,在验券接口传入
    private String verify_token;

    //加密券码, 在验券接口传入
    private String[] encrypted_codes;

    //核销的抖音门店id
    private String poi_id;

}
