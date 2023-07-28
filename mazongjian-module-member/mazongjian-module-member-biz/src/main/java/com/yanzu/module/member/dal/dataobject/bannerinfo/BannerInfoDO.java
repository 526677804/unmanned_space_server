package com.yanzu.module.member.dal.dataobject.bannerinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 广告管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_banner_info")
@KeySequence("member_banner_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BannerInfoDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 标题
     */
    private String title;
    /**
     * 跳转地址/页面路径
     */
    private String jumpUrl;
    /**
     * 排序
     */
    private Integer sortId;
    /**
     * 广告类型
     *
     * 枚举 {@link TODO member_banner_type 对应的类}
     */
    private Byte type;

}
