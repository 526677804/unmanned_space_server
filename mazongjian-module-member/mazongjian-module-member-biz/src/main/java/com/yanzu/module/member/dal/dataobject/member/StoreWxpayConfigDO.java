package com.yanzu.module.member.dal.dataobject.member;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

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
     * 门店ID
     */
    private Long storeId;
    /**
     * 小程序id
     */
    private String appId;
    /**
     * 微信支付商户号
     */
    private String mchId;
    /**
     * 微信支付商户密钥
     */
    private String mchKey;
    /**
     * 证书key
     */
    private String apiclientKey;
    /**
     * 证书cert
     */
    private String apiclientCert;

}
