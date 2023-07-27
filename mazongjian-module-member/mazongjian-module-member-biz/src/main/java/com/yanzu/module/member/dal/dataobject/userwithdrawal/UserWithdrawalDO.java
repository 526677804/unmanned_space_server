package com.yanzu.module.member.dal.dataobject.userwithdrawal;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 用户提现 DO
 *
 * @author 芋道源码
 */
@TableName("member_user_withdrawal")
@KeySequence("member_user_withdrawal_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWithdrawalDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 编号
     */
    private String no;
    /**
     * 用户Id
     */
    private Long userId;
    /**
     * 提现金额
     */
    private BigDecimal money;
    /**
     * 完成时间
     */
    private LocalDateTime finishTime;
    /**
     * 状态
     *
     * 枚举 {@link TODO member_user_withdrawal 对应的类}
     */
    private Boolean status;

}
