package com.yanzu.module.member.dal.dataobject.clearbill;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 保洁账单管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_clear_bill")
@KeySequence("member_clear_bill_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClearBillDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;

    private Long storeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 结算金额
     */
    private BigDecimal money;
    /**
     * 订单数量
     */
    private Integer orderNum;
    /**
     * 订单ids
     */
    private String orderIds;

}
