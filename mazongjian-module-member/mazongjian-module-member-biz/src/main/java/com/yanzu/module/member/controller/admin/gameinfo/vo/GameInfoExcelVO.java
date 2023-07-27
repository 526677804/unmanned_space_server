package com.yanzu.module.member.controller.admin.gameinfo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;
import com.yanzu.framework.excel.core.annotations.DictFormat;
import com.yanzu.framework.excel.core.convert.DictConvert;


/**
 * 在线组局管理 Excel VO
 *
 * @author 芋道源码
 */
@Data
public class GameInfoExcelVO {

    @ExcelProperty("对局ID")
    private Long gameId;

    @ExcelProperty("门店ID")
    private Long storeId;

    @ExcelProperty("房间ID")
    private Long roomId;

    @ExcelProperty("规则描述")
    private String ruleDesc;

    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @ExcelProperty("用户id")
    private Long userId;

    @ExcelProperty(value = "状态", converter = DictConvert.class)
    @DictFormat("member_game_status") // TODO 代码优化：建议设置到对应的 XXXDictTypeConstants 枚举类中
    private Integer status;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
