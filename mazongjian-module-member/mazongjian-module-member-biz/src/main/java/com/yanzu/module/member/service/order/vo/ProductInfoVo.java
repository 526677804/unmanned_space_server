package com.yanzu.module.member.service.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ProductInfoVo {

    @Schema(name = "三方商品id")
    private String app_product_id;

    @Schema(name = "三方sku_id（次卡时 为对应次卡关联团购所设置的skuid）")
    private String app_sku_id;

    @Schema(name = "美团侧商品id")
    private String product_id;

    @Schema(name = "美团侧商品sku_id")
    private String sku_id;

    @Schema(name = "商品个数，默认为1")
    private String quantity;

    @Schema(name = "总金额")
    private String amount;

    @Schema(name = "商品说明信息，用于管道疏通类服务商模式和保洁类团单的加价信息")
    private String desc;

    @Schema(name = "商品类型，0-普通团购，205-次卡")
    private String product_type;

    @Schema(name = "次卡关联的团购项目ID")
    private String related_product;

    @Schema(name = "次卡总计服务次数")
    private String card_total_number;

    @Schema(name = "次卡本次消费的服务次数")
    private String card_consumption_number;

    @Schema(name = "次卡剩余的可消费次数")
    private String card_remain_number;

    @Schema(name = "用户指定的优先服务人员id，在美团侧通过接口生成的服务人员id，无特殊指定为0")
    private String adv_service_techId;

    @Schema(name = "当前预约属于周期性预约时会传该id，同属于一个周期预约的预约该id相同")
    private String period_orderId;

    @Schema(name = "平台统一订单id")
    private String uni_order_id;
}
