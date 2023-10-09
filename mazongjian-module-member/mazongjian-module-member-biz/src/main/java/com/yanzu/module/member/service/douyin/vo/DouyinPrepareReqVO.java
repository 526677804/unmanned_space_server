package com.yanzu.module.member.service.douyin.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/8 14:21
 */
@Data
public class DouyinPrepareReqVO {

    //从二维码解析出来的标识（传参前需要先进行URL编码）
    private String encrypted_data;

    //原始的抖音团购券码  (encrypted_data/code必须二选一)
    private String code;

}
