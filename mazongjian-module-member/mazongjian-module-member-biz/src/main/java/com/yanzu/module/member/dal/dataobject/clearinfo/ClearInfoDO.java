package com.yanzu.module.member.dal.dataobject.clearinfo;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 保洁信息管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_clear_info")
@KeySequence("member_clear_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClearInfoDO extends BaseDO {

    /**
     * 清洁记录id
     */
    @TableId
    private Long clearId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 订单编号
     */
    private String orderNo;
    /**
     * 清洁图片
     */
    private String imgs;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 投诉的照片
     */
    private String complaintImgs;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结算时间
     */
    private LocalDateTime settlementTime;
    /**
     * 状态
     *
     * 枚举 {@link TODO member_clear_info_status 对应的类}
     */
    private String status;

}
