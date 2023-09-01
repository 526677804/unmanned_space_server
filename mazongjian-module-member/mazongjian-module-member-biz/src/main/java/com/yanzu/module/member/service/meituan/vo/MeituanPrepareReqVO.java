package com.yanzu.module.member.service.meituan.vo;

import lombok.Data;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.meituan
 * @DESCRIPTION: 查券参数vo
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/9/1 13:51
 */
@Data
public class MeituanPrepareReqVO extends MeituanCommonParam {

    //团购券码，必须未验证
    private String receipt_code;

    //美团点评店铺id，必须是团购的适用门店
    private String open_shop_uuid;
}
