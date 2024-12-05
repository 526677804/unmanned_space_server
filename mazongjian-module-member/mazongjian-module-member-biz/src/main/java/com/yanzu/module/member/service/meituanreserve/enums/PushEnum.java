package com.yanzu.module.member.service.meituanreserve.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
public enum PushEnum {
    BOOKING_RESULT(5810003,"预订结果同步"),
    CANCEL_RESERVATION(5810001,"取消预订"),
    WITHDRAW_REFUND(5810005,"用户撤销退款通知"),
    CHANGE_RESULT(5810007,"改约结果通知三方"),
    NOTIFY_VERIFICATION(5810027,"通知三方核销"),
    PREMIUM_PAYMENT(5810029,"补价支付结果"),
    BOOKING_VERIFICATION(5810031,"预订核销同步"),
    MULTI_COMMODITY_INVENTORY_BATCH(5810033,"多商品库存批量查询"),
    GET_INVENTORY_IN_BULK(5810025,"批量获取库存"),
    VERIFICATION_STATUS_QUERY(5810023,"核销状态查询"),
    SINGLE_QUERY_INVENTORY(5810021,"单个查询库存"),
    ROOM_INFORMATION(5810019,"查询三方房间信息"),
    START_BOOKING(5810017,"开始预订"),
    CHANGE_SUBMISSION(5810015,"改约提交"),
    CANCEL_BOOKING_REVIEW(5810013,"取消预订审核"),
    FIXED_TIME_POINT_SERVICE_PERSONNEL_INVENTORY(5810011,"批量查询商家固定时间点服务人员库存"),
    HOME_INDUSTRY_INVENTORY_INQUIRY(5810009,"到家行业库存查询");


    private Integer value;
    private String desc;

    private static final Map<Integer, PushEnum> ENUM_MAP = java.util.Arrays.stream(values())
            .collect(Collectors.toMap(PushEnum::getValue, e -> e));

    public static PushEnum fromValue(Integer value) {
        return ENUM_MAP.get(value);
    }

}
