package com.yanzu.module.member.dal.dataobject.storeuser;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

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
     * 余额
     */
    private BigDecimal balance;
    /**
     * 赠送余额
     */
    private BigDecimal giftBalance;

    /**
     * 会员黑名单 0否 1是
     */
    private Integer vipBlacklist;

    private Date addTime;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 会员等级
     */
    private Byte vipLevel;

    /**
     * 总积分
     */
    private Integer totalScore;

}
