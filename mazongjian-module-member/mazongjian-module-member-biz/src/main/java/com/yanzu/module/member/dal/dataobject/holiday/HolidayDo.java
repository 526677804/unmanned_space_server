package com.yanzu.module.member.dal.dataobject.holiday;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@TableName("member_holiday")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HolidayDo {

    @Schema(name = "时间")
    private String dateTime;

    @Schema(name = "类型 0工作日期 1周末 2节假日")
    private String type;

    @Schema(name = "备注")
    private String remark;

}
