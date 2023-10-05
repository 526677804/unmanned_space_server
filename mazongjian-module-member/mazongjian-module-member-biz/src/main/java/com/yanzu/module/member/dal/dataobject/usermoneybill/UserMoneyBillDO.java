package com.yanzu.module.member.dal.dataobject.usermoneybill;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户账单明细 DO
 *
 * @author 芋道源码
 */
@TableName("member_user_money_bill")
@KeySequence("member_user_money_bill_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserMoneyBillDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    private Long storeId;
    /**
     * 类型
     *
     * 枚举 {@link TODO member_user_money_bill_type 对应的类}
     */
    private Integer type;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 金额类型
     *
     * 枚举 {@link TODO member_user_money_type 对应的类}
     */
    private Integer moneyType;
    /**
     * 当时总账户余额
     */
    private BigDecimal totalMoney;
    /**
     * 当时总赠送余额
     */
    private BigDecimal totalGiftMoney;
    /**
     * 备注
     */
    private String remark;

}
