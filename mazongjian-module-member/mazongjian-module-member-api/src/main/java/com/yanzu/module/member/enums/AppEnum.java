package com.yanzu.module.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppEnum {


//    @Getter
//    public static final String PAY_ORDER_REDIS_SET = "PAY_ORDER_REDIS_SET";

    @Getter
    public static final String WX_PAY_ORDER = "WX_PAY_ORDER_%s";

    @Getter
    public static final String DOUYIN_CLIENT_TOKEN = "DOUYIN_CLIENT_TOKEN";

    @Getter
    public static final String SMYOO_IOT_TOKEN = "SMYOO_IOT_TOKEN";

    //miniapp广告类型
    @Getter
    @AllArgsConstructor
    public enum banner_type {
        //        首页	1
//        个人中心	2
        INDEX(1),
        USER_CENTER(2);


        private final Integer value;
    }

    //miniapp用户加盟信息状态
    @Getter
    @AllArgsConstructor
    public enum franchise_status {

        //已提交	0
        //已跟进	1
        //已成交	2
        COMMIT(0),
        FOLLOW(1),
        FINISH(2);


        private final Integer value;
    }

    //miniapp用户提现记录状态
    @Getter
    @AllArgsConstructor
    public enum user_withdrawal {
        //已提交	0
        //已完成	1
        COMMIT(1),
        FINISH(2);


        private final Integer value;
    }

    //miniapp用户账单金额类型
    @Getter
    @AllArgsConstructor
    public enum user_money_type {
        //账户余额	1
        //赠送余额	2
        MONEY(1),
        GIFT_MONEY(2);


        private final Integer value;
    }

    //miniapp用户账单明细类型
    @Getter
    @AllArgsConstructor
    public enum user_money_bill_type {
        //在线充值	1
        //充值赠送	2
        //订单支付	3
        //订单退款	4
        //管理员赠送	5
        //管理员清空	6
        RECHARGE(1),
        GIFT(2),
        PAY(3),
        REFUND(4),
        ADMIN_GIFT(5),
        ADMIN_CLEAN(6);


        private final Integer value;
    }

    //miniapp房间标签
    @Getter
    @AllArgsConstructor
    public enum room_label {

        INDEX(1),
        USER_CENTER(2);


        private final Integer value;
    }

    //miniapp房间使用状态
    @Getter
    @AllArgsConstructor
    public enum room_status {
        //禁用	0
        //空闲	1
        //待清洁	2
        //使用中	3
        //已预约	4
        DISABLE(0),
        ENABLE(1),
        CLEAR(2),
        USED(3),
        PENDING(4),
        ;


        private final Integer value;
    }

    @Getter
    @AllArgsConstructor
    public enum store_status {
        //正常	0
        //审核中	1
        //已到期	2
        ENABLE(0),
        AUDIT(1),
        EXPIRE(2),
        ;


        private final Integer value;
    }

    //miniapp房间类型
    @Getter
    @AllArgsConstructor
    public enum room_type {

        //小包	1
        //中包	2
        //大包	3
        //豪包	4
        //商务包	5

        XIAO(1),
        ZHONG(2),
        DA(3),
        HAO(4),
        SW(5);


        private final Integer value;
    }

    //miniapp订单支付状态
    @Getter
    @AllArgsConstructor
    public enum order_status {
        //未开始	0
        //进行中	1
        //已完成 2
        //已取消	3
        //待支付	4

        PENDING(0),
        START(1),
        FINISH(2),
        CANCEL(3),
        UNPAID(4),
        ;


        private final Integer value;
    }//miniapp订单支付方式

    @Getter
    @AllArgsConstructor
    public enum order_pay_type {
        //微信	1
        //余额	2
        //团购	3
        //套餐	4

        WEIXIN(1),
        WALLET(2),
        TUANGOU(3),
        PKG(4);
//        DAZONG(4),
//        DOUYIN(5);


        private final Integer value;
    }

    //miniapp在线组局状态
    @Getter
    @AllArgsConstructor
    public enum game_status {
        //组局中	0
        //已组局	1
        //已支付	2
        //已失效	3
        //房主解散	4

        PROGRESS(0),
        SUCCESS(1),
        PAY(2),
        EXPIRED(3),
        CANCEL(4);


        private final Integer value;
    }

    //miniapp充值优惠规则状态
    @Getter
    @AllArgsConstructor
    public enum discount_rules_status {

        //禁用	0
        //启用	1
        //过期	2
        DISABLE(0),
        ENABLE(1),
        EXPIRE(2);


        private final Integer value;
    }

    //miniapp设备类型
    @Getter
    @AllArgsConstructor
    public enum device_type {
        //门禁	1
        //空开	2
        //云喇叭	3
        //灯具 4
        //密码锁 5
        //网关 6
        //插座 7
        //锁球器控制器（12V） 8
        //人脸门禁机 9
        //智能语音喇叭 10
        //二维码识别器 11
        //红外控制器 12
        //三路控制器 13

        DOOR(1),
        ELECTRIC(2),
        SOUND(3),
        LIGHT(4),
        LOCK(5),
        GATEWAY(6),
        SOCKET(7),
        TAIQIU_12V(8),
        FACE(9),
        SOUND_V2(10),
        QR(11),
        IR(12),
        CONTROL_3(13),


        ;


        private final Integer value;
    }

    //miniapp优惠券状态
    @Getter
    @AllArgsConstructor
    public enum coupon_status {
        //待使用	0
        //已使用	1
        //已过期	2

        AVAILABLE(0),
        USED(1),
        EXPIRE(2);


        private final Integer value;
    }

    //miniapp房间类别
    @Getter
    @AllArgsConstructor
    public enum room_class {
        //棋牌	0
        //台球	1
        //自习室	2

        QIPAI(0),
        TAIQIU(1),
        ZIXISHI(2);

        private final Integer value;
    }

    //miniapp优惠券类型
    @Getter
    @AllArgsConstructor
    public enum coupon_type {

        DIKOU(1),
        MANJIAN(2),
        JIASHI(3);

        private final Integer value;
    }


    //miniapp保洁信息状态
    @Getter
    @AllArgsConstructor
    public enum clear_info_status {
//        状态 0待接单 1已接单 2已开始 3已完成 4已取消 5被投诉 6已结算

        DEFAULT(0),
        JIEDAN(1),
        START(2),
        FINISH(3),
        CANCEL(4),
        TOUSU(5),
        JIESUAN(6);


        private final Integer value;
    }

    //miniapp用户类型
    @Getter
    @AllArgsConstructor
    public enum member_user_type {
        MEMBER(11),
        BOSS(12),
        ADMIN(13),
        CLEAR(14);

        private final Integer value;
    }

    //miniapp团购券类型
    @Getter
    @AllArgsConstructor
    public enum member_group_no_type {
        MEITUAN(1),
        DOUYIN(2),
        KUAISHOU(3),
        ;

        private final Integer value;
    }

}
