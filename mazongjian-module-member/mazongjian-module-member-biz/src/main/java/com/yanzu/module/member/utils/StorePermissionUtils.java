package com.yanzu.module.member.utils;

import com.yanzu.module.member.enums.AppEnum;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.AUTH_PROMISSION_ERROR;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.utils
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/8/28 14:27
 */
public class StorePermissionUtils {

    public static void checkBoss(Integer userType) {
        //仅加盟商使用
        if (userType.compareTo(AppEnum.member_user_type.BOSS.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
    }

    public static void checkAdmin(Integer userType) {
        //加盟商使用或管理员使用
        if (userType.compareTo(AppEnum.member_user_type.BOSS.getValue()) != 0 || userType.compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
    }
}
