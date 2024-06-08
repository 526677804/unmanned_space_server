package com.yanzu.module.member.dal.dataobject.pkginfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import com.yanzu.framework.mybatis.core.type.IntegerListTypeHandler;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 套餐信息 DO
 *
 * @author 超级管理员
 */
@TableName(value = "member_pkg_info", autoResultMap = true)
@KeySequence("member_pkg_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PkgInfoDO extends BaseDO {

    /**
     * 套餐id
     */
    @TableId
    private Long pkgId;
    /**
     * 套餐名称
     */
    private String pkgName;
    /**
     * 抵扣时长
     */
    private Integer hours;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 房间类型
     */
    private Integer roomType;
    /**
     * 可用时间
     */
    @TableField(typeHandler = IntegerListTypeHandler.class)
    private List<Integer> enableTime;
    /**
     * 可用星期
     */
    @TableField(typeHandler = IntegerListTypeHandler.class)
    private List<Integer> enableWeek;
    /**
     * 节假日可用
     */
    private Boolean enableHoliday;
    /**
     * 销售价格
     */
    private BigDecimal price;
    /**
     * 购买后过期时间
     */
    private Integer expireDay;
    /**
     * 最大购买数量
     */
    private Integer maxNum;
    /**
     * 是否启用
     */
    private Boolean enable;

    /**
     * 排序id
     */
    private Integer sortId;
    /**
     * 支持余额购买
     */
    private Boolean balanceBuy;


    /**
     * 创建用户id
     */
    private Long createUserId;

}
