package com.yanzu.module.member.dal.dataobject.deviceuseinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备使用记录 DO
 *
 * @author 芋道源码
 */
@TableName("member_device_use_info")
@KeySequence("member_device_use_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceUseInfoDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备sn
     */
    private String deviceNo;
    /**
     * 命令
     */
    private String cmd;

}
