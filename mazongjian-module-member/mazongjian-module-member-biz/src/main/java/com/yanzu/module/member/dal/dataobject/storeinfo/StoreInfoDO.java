package com.yanzu.module.member.dal.dataobject.storeinfo;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

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
     * 房间标签
     */
    private String label;
    /**
     * 工作日折扣
     */
    private Integer workDiscount;
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

}
