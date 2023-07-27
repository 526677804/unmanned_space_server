package com.yanzu.module.member.dal.dataobject.deviceinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_device_info")
@KeySequence("member_device_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceInfoDO extends BaseDO {

    /**
     * 设备id
     */
    @TableId
    private Long deviceId;
    /**
     * 设备sn
     */
    private String deviceSn;
    /**
     * 设备类型
     *
     * 枚举 {@link TODO member_device_type 对应的类}
     */
    private Integer type;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 状态
     */
    private Integer status;

}
