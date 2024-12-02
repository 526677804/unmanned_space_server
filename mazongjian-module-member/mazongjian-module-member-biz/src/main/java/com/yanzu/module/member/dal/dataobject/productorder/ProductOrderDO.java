package com.yanzu.module.member.dal.dataobject.productorder;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@TableName("product_order")
@KeySequence("product_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long orderId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 用户Id
     */
    private Long userId;

    /**
     * 产品信息
     */
    private String productInfo;

    /**
     * 商品总数
     */
    private Long totalNum;

    /**
     * 订单总价
     */
    private BigDecimal totalPrice;

    /**
     * 实际支付金额
     */
    private BigDecimal payPrice;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 订单状态(0:待支付,1:已支付,2:已完成)
     */
    private Long status;

    /**
     * 支付方式( 1微信 2余额 3团购)
     */
    private Long payType;

    /**
     * 备注
     */
    private String mark;

    /**
     * 唯一id
     */
    private String unique;

}
