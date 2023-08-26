package com.yanzu.module.member.controller.app.order.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.yanzu.framework.common.util.date.DateUtils.*;

@Data
public class TimeRange {

    private LocalDateTime start;

    private LocalDateTime end;

    public TimeRange(LocalDateTime start, LocalDateTime end) {
        this.start = start;
        this.end = end;
    }

}