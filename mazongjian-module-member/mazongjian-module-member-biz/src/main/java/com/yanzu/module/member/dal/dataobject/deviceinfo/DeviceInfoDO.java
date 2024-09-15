package com.yanzu.module.member.dal.dataobject.deviceinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

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
     * 设备数据
     */
    private String deviceData;

    /**
     * 共享设备
     */

    private Boolean share;
    /**
     * 设备类型
     *
     */
    private Integer type;
    /**
     * 房间id
     */
    private Long storeId;


    private Long roomId;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 信号强度
     */
    private Integer rssi;


}
