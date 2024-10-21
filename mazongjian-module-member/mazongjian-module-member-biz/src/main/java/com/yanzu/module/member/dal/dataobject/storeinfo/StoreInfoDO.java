package com.yanzu.module.member.dal.dataobject.storeinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 门店管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_store_info")
@KeySequence("member_store_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreInfoDO extends BaseDO {

    /**
     * 门店ID
     */
    @TableId
    private Long storeId;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 门店富文本详情
     */
    private String content;
    /**
     * 缩略图url
     */
    private String headImg;
    /**
     * 门店环境照片
     */
    private String storeEnvImg;

    private String bannerImg;
    /**
     * 门店公告
     */
    private String notice;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 经度
     */
    private Double lon;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 门店状态
     */
    private Integer status;
    /**
     * wifi信息
     */
    private String wifiInfo;

    /**
     * wifi密码
     */
    private String wifiPwd;

    /**
     * 简洁模式
     */
    private Boolean simpleModel;
    /**
     * 房间标签
     */
    private String label;

    /**
     * 客服电话
     */
    private String kefuPhone;

    /*订单通知webhook*/
    private String orderWebhook;

    /*组局通知webhook*/
    private String gameWebhook;

    /*抖音poiId*/
    private String douyinPoiId;
    /**
     * 房间数量
     */
    private Integer roomNum;
    /**
     * 总收入
     */
    private BigDecimal totalMoney;
    /**
     * 已提现
     */
    private BigDecimal totalWithdrawal;


    @Schema(description = "订单清洁时间")
    private Integer clearTime;

    @Schema(description = "清洁时开放")
    private Boolean clearOpen;

    @Schema(description = "二维码照片")
    private String qrCode;

    @Schema(description = "预约按钮照片")
    private String btnImg;

    @Schema(description = "团购按钮照片")
    private String tgImg;

    @Schema(description = "充值按钮照片")
    private String czImg;

    @Schema(description = "wifi照片")
    private String wifiImg;

    @Schema(description = "切换门店照片")
    private String qhImg;

    @Schema(description = "一键开门照片")
    private String openImg;

    @Schema(description = "联系客服照片")
    private String kfImg;

    @Schema(description = "显示通宵价格")
    private Boolean showTxPrice;

    @Schema(description = "通宵开始小时")
    private Integer txStartHour;

    @Schema(description = "通宵小时时长")
    private Integer txHour;

    @Schema(description = "延时关灯")
    private Boolean delayLight;

    @Schema(description = "工作日价格")
    private Boolean workPrice;

    @Schema(description = "订单门禁常开")
    private Boolean orderDoorOpen;

    @Schema(description = "保洁任意开门")
    private Boolean clearOpenDoor;


    @Schema(description = "过期时间")
    private LocalDateTime expireTime;

}
