package com.yanzu.module.member.service.douyin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.douyin.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/10/9 12:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DouyinCancelReqVO {

    //代表券码一次核销的唯一标识（验券时返回）
    private String verify_id;

    //代表一张券码的标识（验券时返回）
    private String certificate_id;

}
