package com.yanzu.module.member.controller.app.index.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppOrderTimeVO {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    public AppOrderTimeVO(Date startTime, Date endTime) {
        this.startTime = startTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        this.endTime = endTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
