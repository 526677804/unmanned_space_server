package com.yanzu.module.member.dal.dataobject.orderinfo;

import lombok.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_order_info")
@KeySequence("member_order_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderInfoDO extends BaseDO {

    /**
     * 订单id
     */
    @TableId
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 房间id
     */
    private Long roomId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 订单开始时间
     */
    private Date startTime;
    /**
     * 订单结束时间
     */
    private Date endTime;
    /**
     * 订单价格
     */
    private BigDecimal price;
    /**
     * 工作日折扣
     */
    private Integer workDiscount;
    /**
     * 实际支付价格
     */
    private BigDecimal payPrice;
    /**
     * 退款价格
     */
    private BigDecimal refundPrice;
    /**
     * 支付方式
     * <p>
     * 枚举 {@link TODO member_order_pay_type 对应的类}
     */
    private Integer payType;
    /**
     * 团购券码
     */
    private String groupPayNo;
    /**
     * 优惠券Id
     */
    private Long couponId;
    /**
     * 状态
     * <p>
     * 枚举 {@link TODO member_order_status 对应的类}
     */
    private Integer status;

}
