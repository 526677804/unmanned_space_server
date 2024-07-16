package com.yanzu.module.member.dal.dataobject.member;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 门店微信支付配置 DO
 *
 * @author MrGuan
 */
@TableName("member_store_wxpay_config")
@KeySequence("member_store_wxpay_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreWxpayConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;

    /**
     * 商户的appId
     */
    private String appId;

    /**
     * 门店ID
     */
    private Long storeId;

    /**
     * 微信支付商户号
     */
    private String mchId;

    /**
     * 微信支付商户密钥
     */
    private String mchKey;

    /**
     * p12证书
     */
    private String p12;

    /**
     * 服务商支付模式
     */
    private Boolean serviceModel;

    /**
     * 是否分账
     */
    private Boolean split;

    /**
     * 分账比例
     */
    private Integer splitProp;


}
