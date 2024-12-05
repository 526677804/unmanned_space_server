package com.yanzu.module.member.service.order.vo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 门店管理 DO
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoTenantIdVo {

    /**
     * 门店ID
     */
    @TableId
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;


    private Long tenantId;

}
