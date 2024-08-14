package com.yanzu.module.member.dal.dataobject.storesound;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

@TableName("member_store_sound_info")
@KeySequence("member_store_sound_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSoundInfoDO extends BaseDO {
    /**
     * ID
     */
    @TableId
    private Long soundId;
    /**
     * 门店ID
     */
    private Long storeId;

    private String welcomeText;

    private String endText30;
    private String endText5;
    private String nightText;
}
