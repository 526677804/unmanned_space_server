package com.yanzu.module.member.enums;

import com.yanzu.framework.common.exception.ErrorCode;

/**
 * Member 错误码枚举类
 * <p>
 * member 系统，使用 1-004-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 用户相关  1004001000============
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1004001000, "用户不存在");
    ErrorCode USER_PASSWORD_FAILED = new ErrorCode(1004001001, "密码校验失败");

    // ========== AUTH 模块 1004003000 ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1004003000, "登录失败，账号密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1004003001, "登录失败，账号被禁用");
    ErrorCode AUTH_PROMISSION_ERROR = new ErrorCode(1004003002, "暂无操作权限");
    ErrorCode AUTH_TOKEN_EXPIRED = new ErrorCode(1004003004, "Token 已经过期");
    ErrorCode AUTH_THIRD_LOGIN_NOT_BIND = new ErrorCode(1004003005, "未绑定账号，需要进行绑定");
    ErrorCode AUTH_WEIXIN_MINI_APP_PHONE_CODE_ERROR = new ErrorCode(1004003006, "获得手机号失败");

    // ========== app相关 1004004000 ==========
    ErrorCode NOT_START_ORDER = new ErrorCode(1004004001, "没有进行中的订单！");
    ErrorCode DEVICE_OPRATION_ERROR = new ErrorCode(1004004002, "设备操作失败！");
    ErrorCode ORDER_STATUS_NOT_START_ERROR = new ErrorCode(1004004003, "请先开始订单，再操作开门！");
    ErrorCode DISCOUNTRULE_REPETITION_ERROR = new ErrorCode(1004004024, "该充值金额已存在其他规则中，不允许重复添加！");
    ErrorCode GAME_CREATE_NUM_MAX_ERROR = new ErrorCode(1004004030, "每日允许创建5条组局信息，请明天再试！");
    ErrorCode GAME_DELETE_USER_ERROR = new ErrorCode(1004004031, "只有组队中或未支付的对局才可以踢出玩家！");
    ErrorCode GAME_JOIN_USER_ERROR = new ErrorCode(1004004032, "只有组队中或已加入的对局才能操作！");
    ErrorCode GAME_MAX_USER_ERROR = new ErrorCode(1004004033, "对局人数已满！");
    ErrorCode GAME_DELETE_ME_ERROR = new ErrorCode(1004004034, "不能踢出自己！");


    ErrorCode DATA_NOT_EXISTS = new ErrorCode(1004005000, "数据不存在");
    ErrorCode OPRATION_ERROR = new ErrorCode(1004005001, "非法操作");

}
