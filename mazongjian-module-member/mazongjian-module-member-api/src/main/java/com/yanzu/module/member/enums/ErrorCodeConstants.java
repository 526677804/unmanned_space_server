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
    ErrorCode AUTH_USER_PHONE_ERROR = new ErrorCode(1004003007, "该手机号未注册或未绑定用户！");
    ErrorCode AUTH_USER_BIND_MINIAPP_ERROR = new ErrorCode(1004003008, "该用户未授权微信登录，无法下单！");
    ErrorCode USER_WEIXIN_PAY_ERROR = new ErrorCode(1004003009, "微信支付失败！");
    ErrorCode USER_WEIXIN_PAY_REFUND_ERROR = new ErrorCode(1004003010, "微信退款失败！请联系管理员处理");
    ErrorCode ADMIN_WEIXIN_PAY_REFOUND_ERROR = new ErrorCode(1004003011, "微信退款失败!该订单无法退款");

    // ========== app相关 1004004000 ==========
    ErrorCode NOT_START_ORDER = new ErrorCode(1004004001, "没有进行中的订单！");
    ErrorCode DEVICE_OPRATION_ERROR = new ErrorCode(1004004002, "设备操作失败！");
    ErrorCode CLEAR_USER_DELETE_ERROR = new ErrorCode(1004004003, "请结算完所有已完成的任务，再删除用户！");
    ErrorCode ORDER_STATUS_NOT_START_ERROR = new ErrorCode(1004004004, "请先开始订单，再操作开门！");
    ErrorCode ORDER_START_TIME_ERROR = new ErrorCode(1004004005, "订单开始时间不能小于当前时间！");
    ErrorCode ORDER_START_TIME_GT_END_ERROR = new ErrorCode(1004004006, "订单开始时间不能小于结束时间！");
    ErrorCode ORDER_START_TIME_MAX_ERROR = new ErrorCode(1004004007, "订单开始时间最早不能超过5天！");
    ErrorCode ORDER_TIME_CHECK_ERROR = new ErrorCode(1004004008, "您选择的时间段该房间不可用，请修改时间或更换房间！");
    ErrorCode TIME_UNIT_ERROR = new ErrorCode(1004004009, "时间单位错误，必须以0.5小时/30分钟为一个单位！");
    ErrorCode PAY_TYPE_ERROR = new ErrorCode(1004004010, "支付方式选择错误！");
    ErrorCode ORDER_TIME_MIN_ERROR = new ErrorCode(1004004011, "选择的时长不能低于4个小时！");
    ErrorCode COUPON_NOT_FOUND_ERROR = new ErrorCode(1004004012, "选择的优惠券不存在！");
    ErrorCode COUPON_MIN_USER_PRICE_ERROR = new ErrorCode(1004004013, "选择的优惠券未达到使用门槛！");
    ErrorCode MEMBER_BALANCE_MIN_ERROR = new ErrorCode(1004004014, "账户余额不足，请充值后重试！");
    ErrorCode ORDER_WEIXIN_PAY_ERROR = new ErrorCode(1004004015, "微信支付失败！");
    ErrorCode ORDER_STATUS_CANCEL_OPRATION_ERROR = new ErrorCode(1004004016, "订单已取消，不支持续费！");
    ErrorCode ORDER_STATUS_FINISH_OPRATION_ERROR = new ErrorCode(1004004017, "订单已结束超过5分钟，不支持续费！请重新下单");
    ErrorCode ORDER_START_OPRATION_ERROR = new ErrorCode(1004004018, "订单已开始！");
    ErrorCode ORDER_CANCEL_OPRATION_ERROR = new ErrorCode(1004004019, "无法取消当前订单！仅允许取消下单不超过5分钟，且状态为未开始、进行中的订单！");
    ErrorCode ADMIN_ORDER_CANCEL_OPRATION_ERROR = new ErrorCode(1004004020, "无法取消当前订单!管理员仅允许取消状态为未开始、进行中的订单！");
    ErrorCode ORDER_START_TIME_LT_NOW_ERROR = new ErrorCode(1004004021, "开始时间已经小于当前时间5分钟，请重新选择开始时间！");
    ErrorCode ORDER_MAX_END_TIME_ERROR = new ErrorCode(1004004022, "订单总时长不能超过24小时，请重新选择预订时间！");
    ErrorCode DISCOUNTRULE_REPETITION_ERROR = new ErrorCode(1004004024, "该充值金额已存在其他规则中，不允许重复添加！");
    ErrorCode COUPON_USED_ERROR = new ErrorCode(1004004025, "该优惠券已被使用！");
    ErrorCode COUPON_USE_CHECK_ERROR = new ErrorCode(1004004026, "该优惠券不符合使用条件！");
    ErrorCode COUPON_USE_CHECK_STORE_ERROR = new ErrorCode(1004004027, "该优惠券不能在当前门店使用！");
    ErrorCode USER_NO_MONEY_WITHDRAWAL_ERROR = new ErrorCode(1004004028, "您当前没有可提现的收入！");
    ErrorCode GAME_CREATE_NUM_MAX_ERROR = new ErrorCode(1004004030, "每日允许创建5条组局信息，请明天再试！");
    ErrorCode GAME_DELETE_USER_ERROR = new ErrorCode(1004004031, "只有组队中或未支付的对局才可以踢出玩家！");
    ErrorCode GAME_JOIN_USER_ERROR = new ErrorCode(1004004032, "只有组队中或已加入的对局才能操作！");
    ErrorCode GAME_MAX_USER_ERROR = new ErrorCode(1004004033, "对局人数已满！");
    ErrorCode GAME_DELETE_ME_ERROR = new ErrorCode(1004004034, "不能踢出自己！");
    ErrorCode GAME_START_TIME_ERROR = new ErrorCode(1004004035, "开始时间不能小于当前时间！");
    ErrorCode ORDER_CHANGE_ROOM_ERROR = new ErrorCode(1004004036, "只能更换到等于或小于当前房间级别的房间！");
    ErrorCode ORDER_NOT_FOUND_ERROR = new ErrorCode(1004004037, "当前不存在订单，请先下单！");

    ErrorCode CLEAR_ORDER_NOT_JIEDAN = new ErrorCode(1004004040, "订单已经被其他人抢走！");
    ErrorCode CLEAR_ORDER_STATUS_ERROR = new ErrorCode(1004004041, "订单当前状态不允许进行此操作！");
    ErrorCode USER_TYPE_CHECK_ERROR = new ErrorCode(1004004042, "用户类型检查异常！选择的用户不支持进行此操作！");
    ErrorCode CHECK_STORE_PROMISSION_ERROR = new ErrorCode(1004004050, "选择的门店中包含未授权的门店！");
    ErrorCode MEMBER_PAGE_PARAM_ERROR = new ErrorCode(1004004060, "参数错误！");
    ErrorCode ORDER_PAGE_PARAM_ERROR = new ErrorCode(1004004061, "参数错误！");
    ErrorCode NOT_FINISH_CLEAR_IONF_ERROR = new ErrorCode(1004004062, "该条件下没有可结算的订单！");
    ErrorCode CLEAR_INFO_STATUS_OPRATION_ERROR = new ErrorCode(1004004063, "当前状态不能操作！");
    ErrorCode CLEAR_IMAGE_NOT_FOUNT_ERROR = new ErrorCode(1004004064, "请上传清洁完成的图片！");
    ErrorCode CLEAR_OPEN_DOOR_ERROR = new ErrorCode(1004004065, "当前状态不支持开门！");
    ErrorCode DEVICE_REG_ERROR = new ErrorCode(1004004070, "设备注册到平台失败！");
    ErrorCode DEVICE_BIND_ERROR = new ErrorCode(1004004071, "该设备已经被其他门店/房间绑定！");
    ErrorCode CLEAR_AND_FINISH_ROOM_STATUS_ERROR = new ErrorCode(1004004072, "房间当前状态不允许执行此操作！");
    ErrorCode USRE_ADD_ADMIN_ERROR = new ErrorCode(1004004073, "当前用户不允许进行添加管理员操作！");
    ErrorCode GOURP_NO_PAY_ROOM_TYPE_CHECK_ERROR = new ErrorCode(1004004080, "团购券适用的房间类型，与当前订单预定的房间类型不匹配，请检查!");
    ErrorCode GOURP_NO_PAY_TIME_HOUR_CHECK_ERROR = new ErrorCode(1004004081, "团购券的使用时长，与当前预订时长不匹配，请检查!");
    ErrorCode STORE_MT_TUANGOU_PAY_ERROR = new ErrorCode(1004004082, "当前门店暂不支持团购券支付！请选择其他支付方式！");
    ErrorCode GROUP_NO_CHECK_ERROR = new ErrorCode(1004004083, "团购券验证失败！无效/已使用/已过期");
    ErrorCode GROUP_NO_CANCEL_TIMEOUT_ERROR = new ErrorCode(1004004084, "团购券退款失败,该团购券不支持退款，或已超过退款时效！");
    ErrorCode GROUP_NO_CHECK_TONGXIAO_TIME_ERROR = new ErrorCode(1004004085, "通宵券，仅支持23时以后~次日8时使用！请修改预定时间后重试！");
    ErrorCode TONGXIAO_ORDER_START_ERROR = new ErrorCode(1004004086, "当前预定的时段为通宵场！不支持提前开始消费！");
    ErrorCode TONGXIAO_COUPON_USE_ERROR = new ErrorCode(1004004087, "您选择的优惠券！仅支持通宵场(23时以后~次日8时)使用！");
    ErrorCode STORE_DY_TUANGOU_PAY_ERROR = new ErrorCode(1004004088, "当前店铺暂不支持抖音团购券支付！请选择其他支付方式！");
    ErrorCode GROUP_PAY_WORK_CHECK_ERROR = new ErrorCode(1004004089, "您输入的团购券仅周一至周四可用！请修改预定时间或更换团购券");
    ErrorCode GROUP_PAY_WORK_DAY_CHECK_ERROR = new ErrorCode(1004004090, "您输入的团购券仅工作日可用！请修改预定时间或更换团购券");
    ErrorCode GROUP_PAY_ROOM_TYPE_CHECK_ERROR = new ErrorCode(1004004091, "您输入的团购券仅【小包】可用！请修改预订的包间！");
    ErrorCode ORDER_START_TIQIAN_ERROR = new ErrorCode(1004004092, "订单不允许提前6小时以上开始！");
    ErrorCode CHECK_TONGXIAO_TIME_ERROR = new ErrorCode(1004004093, "团购的通宵场时间为23时以后~次日8时！请修改预定时间后重试！");
    ErrorCode CHECK_TONGXIAO_END_TIME_ERROR = new ErrorCode(1004004094, "通宵场的结束时间只能为08:00！请修改预定时间后重试！");

    ErrorCode GROUP_NO_USE_CHECK_ERROR = new ErrorCode(1004004084, "您输入的团购券不符合订单使用条件！");


    ErrorCode STORE_WX_PAY_CONFIG_NOT_FOUND = new ErrorCode(1004004999, "该门店暂不支持微信支付！");
    ErrorCode DATA_NOT_EXISTS = new ErrorCode(1004005000, "数据不存在");
    ErrorCode DATA_EXISTS_ERROR = new ErrorCode(1004005001, "数据已存在，请不要重复保存！");
    ErrorCode OPRATION_ERROR = new ErrorCode(1004005002, "非法操作");

}
