package com.yanzu.module.member.dal.dataobject.franchiseinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 加盟信息 DO
 *
 * @author 芋道源码
 */
@TableName("member_franchise_info")
@KeySequence("member_franchise_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FranchiseInfoDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人电话
     */
    private String contactPhone;
    /**
     * 留言
     */
    private String message;
    /**
     * 状态
     *
     * 枚举 {@link TODO member_franchise_status 对应的类}
     */
    private Integer status;

}
