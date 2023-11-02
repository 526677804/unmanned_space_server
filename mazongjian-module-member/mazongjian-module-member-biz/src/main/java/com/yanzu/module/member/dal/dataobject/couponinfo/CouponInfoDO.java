package com.yanzu.module.member.dal.dataobject.couponinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_coupon_info")
@KeySequence("member_coupon_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponInfoDO extends BaseDO implements Serializable {

    /**
     * 优惠券ID
     */
    @TableId
    private Long couponId;
    /**
     * 持有用户
     */
    private Long userId;
    /**
     * 创建用户
     */
    private Long createUserId;
    /**
     * 过期时间
     */
    private Date expriceTime;
    /**
     * 优惠券名称
     */
    private String couponName;
    /**
     * 使用门槛
     */
    private BigDecimal minUsePrice;
    /**
     * 优惠券面额
     */
    private BigDecimal price;
    /**
     * 使用门店Id
     */
    private Long storeId;
    /**
     * 使用房间类型
     */
    private Integer roomType;
    /**
     * 优惠券类型
     * <p>
     * 枚举 {member_coupon_type 对应的类}
     */
    private Integer type;
    /**
     * 状态
     * <p>
     * 枚举 {  member_coupon_status 对应的类}
     */
    private Integer status;

}
