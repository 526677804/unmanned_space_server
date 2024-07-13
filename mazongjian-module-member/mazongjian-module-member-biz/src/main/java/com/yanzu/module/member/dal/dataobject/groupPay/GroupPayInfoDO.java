package com.yanzu.module.member.dal.dataobject.groupPay;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 团购支付信息 DO
 *
 * @author MrGuan
 */
@TableName("member_group_pay_info")
@KeySequence("member_group_pay_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPayInfoDO extends BaseDO {

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 团购券名称
     */
    private String groupName;
    /**
     * 团购券编码
     */
    private String groupNo;
    /**
     * 团购商品id
     */
    private String groupShopId;
    /**
     * 团购券信息
     */
    private String ticketInfo;
    /**
     * 价格
     */
    private BigDecimal groupPayPrice;
    /**
     * 团购券类型
     *
     */
    private Integer groupPayType;
    /**
     * 门店
     */
    private Long storeId;
    /**
     * 订单
     */
    private Long orderId;

}
