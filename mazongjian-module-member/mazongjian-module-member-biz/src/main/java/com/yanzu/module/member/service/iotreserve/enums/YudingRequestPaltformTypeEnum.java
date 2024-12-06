package com.yanzu.module.member.service.iotreserve.enums;

import cn.hutool.core.util.ArrayUtil;
import com.yanzu.framework.common.core.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 预订请求平台类型
 *
 * @author 彦祖科技
 */
@AllArgsConstructor
@Getter
public enum YudingRequestPaltformTypeEnum implements IntArrayValuable {

    MT(0, "美团"),

    ;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(YudingRequestPaltformTypeEnum::getType).toArray();

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

    public static YudingRequestPaltformTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
