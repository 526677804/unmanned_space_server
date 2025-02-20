package com.yanzu.module.member.dal.dataobject.storevipconfig;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门店会员配置 DO
 *
 * @author 超级管理员
 */
@TableName("member_store_vip_config")
@KeySequence("member_store_vip_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreVipConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long vipId;
    /**
     * 门店
     */
    private Long storeId;
    /**
     * 会员名称
     */
    private String vipName;
    /**
     * 等级 
     */
    private Byte vipLevel;
    /**
     * 折扣
     */
    private Byte vipDiscount;
    /**
     * 积分门槛
     */
    private Integer score;

}
