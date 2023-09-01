package com.yanzu.module.member.dal.dataobject.storemeituaninfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 门店美团平台信息 DO
 *
 * @author 芋道源码
 */
@TableName("member_store_meituan_info")
@KeySequence("member_store_meituan_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreMeituanInfoDO extends BaseDO {

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
     * open_shop_uuid
     */
    private String openShopUuid;
    /**
     * access_token
     */
    private String accessToken;
    /**
     * expires_in
     */
    private LocalDateTime expiresIn;
    /**
     * refresh_token
     */
    private String refreshToken;
    /**
     * remain_refresh_count
     */
    private Integer remainRefreshCount;

}
