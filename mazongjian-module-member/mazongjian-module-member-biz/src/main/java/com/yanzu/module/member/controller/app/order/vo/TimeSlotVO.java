package com.yanzu.module.member.controller.app.order.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.order.vo
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 15:43
 */
@Data
public class TimeSlotVO {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

}
