package com.yanzu.module.member.dal.dataobject.discountrules;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 充值优惠规则管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_discount_rules")
@KeySequence("member_discount_rules_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountRulesDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long discountId;
    /**
     * 支付金额
     */
    private BigDecimal payMoney;
    /**
     * 赠送金额
     */
    private BigDecimal giftMoney;
    /**
     * 过期时间
     */
    private LocalDateTime expriceTime;
    /**
     * 适用门店id
     */
    private Long storeId;
    /**
     * 状态
     *
     * 枚举 {@link TODO member_discount_rules_status 对应的类}
     */
    private Integer status;

}
