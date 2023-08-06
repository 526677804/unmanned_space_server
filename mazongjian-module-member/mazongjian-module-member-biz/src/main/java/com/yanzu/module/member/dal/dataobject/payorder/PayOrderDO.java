package com.yanzu.module.member.dal.dataobject.payorder;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 支付订单 DO
 *
 * @author 芋道源码
 */
@TableName("member_pay_order")
@KeySequence("member_pay_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayOrderDO extends BaseDO {

    /**
     * 订单编号
     */
    @TableId
    private Long id;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 订单内容
     */
    private String orderDesc;
    /**
     * 价格
     */
    private Integer price;
    /**
     * 是否已支付
     *
     * 枚举 {@link TODO infra_boolean_string 对应的类}
     */
    private Boolean payStatus;
    /**
     * 支付订单编号
     */
    private String payOrderNo;
    /**
     * 订单支付时间
     */
    private LocalDateTime payTime;
    /**
     * 退款订单编号
     */
    private String payRefundNo;
    /**
     * 退款金额
     */
    private Integer refundPrice;
    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

}
