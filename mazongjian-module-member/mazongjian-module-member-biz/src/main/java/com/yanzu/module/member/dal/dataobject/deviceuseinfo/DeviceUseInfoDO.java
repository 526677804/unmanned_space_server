package com.yanzu.module.member.dal.dataobject.deviceuseinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

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
     * 门店id
     */
    private Long storeId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 命令
     */
    private String cmd;

}
