package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 查券参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanScopeReqVO extends MeituanCommonParam {

    //bid
    private String bid;

    private Integer offset;
    private Integer limit;
}
