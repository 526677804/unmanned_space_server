package com.yanzu.module.member.dal.dataobject.clearinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.time.LocalDateTime;

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
    private Long storeId;
    private Long roomId;
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

    private String complaintDesc;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    private LocalDateTime finishTime;
    /**
     * 结算时间
     */
    private LocalDateTime settlementTime;
    /**
     * 状态
     * <p>
     * 枚举 {@link TODO member_clear_info_status 对应的类}
     */
    private Integer status;

}
