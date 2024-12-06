package com.yanzu.module.member.service.iotreserve.enums;

import cn.hutool.core.util.ArrayUtil;
import com.yanzu.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 预订请求
 *
 * @author 彦祖科技
 */
@AllArgsConstructor
@Getter
public enum YudingRequestTypeEnum implements IntArrayValuable {


    /**
     * 美团请求
     */
    MT_BOOK_RESULT_CALLBACK(1000000001, "预订结果回调"),
    MT_CHECK_OUT_AT_THE_STORE(1000000002, "用户到店核销"),
    MT_CANCEL_BOOKING_REVIEW_RESULT_CALLBACK(1000000003, "取消预订审核结果回调"),
    MT_DDZH_YUDING_UPDATESTOCK(1000000004, "更新三方库存"),
    MT_DDZH_YUDING_UPDATEBOOKRULE(1000000005, "三方门店推送预订规则至平台"),

    //其他平台请求...
    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(YudingRequestTypeEnum::getType).toArray();

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型的标识
     */
    private final String source;

    @Override
    public int[] array() {
        return ARRAYS;
    }

    public static YudingRequestTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
