package com.yanzu.module.member.controller.app.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 15:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema
public class DateSlotVO {

    private Date startTime;

    private Date endTime;

}
