package com.yanzu.module.member.dal.dataobject.couponinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

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
public class CouponInfoDO extends BaseDO {

    /**
     * 优惠券ID
     */
    @TableId
    private Long couponId;
    /**
     * 持有用户
     */
    private String userId;
    /**
     * 创建用户
     */
    private String createUserId;
    /**
     * 过期时间
     */
    private LocalDateTime expriceTime;
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
     * 使用门店Ids
     */
    private String storeIds;
    /**
     * 使用房间Ids
     */
    private String roomIds;
    /**
     * 优惠券类型
     *
     * 枚举 {member_coupon_type 对应的类}
     */
    private Integer type;
    /**
     * 状态
     *
     * 枚举 {  member_coupon_status 对应的类}
     */
    private Integer status;

}
