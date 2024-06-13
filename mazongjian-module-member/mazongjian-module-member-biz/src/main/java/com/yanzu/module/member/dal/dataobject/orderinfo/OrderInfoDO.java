package com.yanzu.module.member.dal.dataobject.orderinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

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
     * 订单key
     */
    private String orderKey;
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
     * 是否通宵
     */
    private Boolean nightLong;
    /**
     * 订单价格
     */
    private BigDecimal price;

    /**
     * 押金
     */
    private BigDecimal deposit;
    /**
     * 工作日折扣
     */
    private Integer workDiscount;
    /**
     * 实际支付价格
     */
    private BigDecimal payPrice;
    /**
     * 团购支付价格
     */
    private BigDecimal groupPayPrice;
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
     * 团购券类型
     */
    private Integer groupPayType;
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
