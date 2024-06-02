package com.yanzu.module.member.dal.dataobject.pkguserinfo;

import lombok.*;

import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户套餐信息 DO
 *
 * @author 超级管理员
 */
@TableName("member_pkg_user_info")
@KeySequence("member_pkg_user_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PkgUserInfoDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 持有用户id
     */
    private Long userId;
    /**
     * 套餐id
     */
    private Long pkgId;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 过期时间
     */
    private LocalDate expireDate;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 状态 0待使用 1已使用 2过期
     */
    private Integer status;

}
