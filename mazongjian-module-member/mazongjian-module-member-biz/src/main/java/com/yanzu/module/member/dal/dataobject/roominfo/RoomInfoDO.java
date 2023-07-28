package com.yanzu.module.member.dal.dataobject.roominfo;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 房间管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_room_info")
@KeySequence("member_room_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomInfoDO extends BaseDO {

    /**
     * 房间id
     */
    @TableId
    private Long roomId;
    /**
     * 房间名称
     */
    private String roomName;
    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 房间类型
     *
     * 枚举 {@link TODO member_room_type 对应的类}
     */
    private Integer type;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 房间标签
     */
    private String label;
    /**
     * 房间照片
     */
    private String imageUrls;
    /**
     * 排序位置
     */
    private Integer sortId;
    /**
     * 禁用开始时间
     */
    private String banTimeStart;
    /**
     * 禁用结束时间
     */
    private String banTimeEnd;
    /**
     * 总完成订单数
     */
    private Integer totalOrderNum;
    /**
     * 总收益
     */
    private BigDecimal totalMoney;
    /**
     * 状态
     *
     * 枚举 {@link TODO member_room_status 对应的类}
     */
    private Integer status;

}
