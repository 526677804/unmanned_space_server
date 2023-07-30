package com.yanzu.module.member.dal.dataobject.storeuser;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 门店用户管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_store_user")
@KeySequence("member_store_user_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreUserDO extends BaseDO {

    /**
     * 收入
     */
    private BigDecimal money;
    /**
     * ID
     */
    @TableId
    private Long id;

    private String name;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 类型
     *
     * 枚举 {member_store_user_type 对应的类}
     */
    private Integer type;

    /**
     * 赠送余额
     */
    private BigDecimal giftBalance;
    /**
     * 状态
     */
    private Integer status;

}
