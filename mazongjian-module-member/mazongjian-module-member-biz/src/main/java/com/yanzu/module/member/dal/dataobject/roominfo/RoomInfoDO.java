package com.yanzu.module.member.dal.dataobject.roominfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

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
     * 枚举 {@link TODO member_room_class 对应的类}
     */
    private String roomName;

    /**
     * 房间简称、语音呼叫时播报的名称
     */
    private String roomCallName;

    /**
     * 房间类别
     */
    private Integer roomClass;

    /**
     * 房间小程序码
     */
    private String qrCode;

    /**
     * 续费二维码
     */
    private String renewCode;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 房间类型
     * <p>
     * 枚举 {@link TODO member_room_type 对应的类}
     */
    private Integer type;
    /**
     * 单价
     */
    private BigDecimal workPrice;
    private BigDecimal price;
    private BigDecimal deposit;
    private BigDecimal tongxiaoPrice;
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
     * 云喇叭音量
     */
    private Integer yunlabaSound;
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
     * <p>
     * 枚举 {@link TODO member_room_status 对应的类}
     */
    private Integer status;

    @Schema(description = "最大提前开始时间")
    private Integer leadHour;

    @Schema(description = "最大提前下单天数")
    private Integer leadDay;

    @Schema(description = "最小下单时间")
    private Integer minHour;

    @Schema(description = "跳过清洁")
    private Boolean jumpClear;




}
