package com.yanzu.module.member.controller.app.store.vo;

import com.yanzu.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AppGetVipBlackListVO extends PageParam {

    private Long storeId;

}
