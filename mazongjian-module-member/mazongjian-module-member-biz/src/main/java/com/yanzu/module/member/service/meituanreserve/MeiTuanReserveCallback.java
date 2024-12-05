package com.yanzu.module.member.service.meituanreserve;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.meituanreserve.vo.MeiTuanReserveReqVo;
import com.yanzu.module.member.service.meituanreserve.enums.PushEnum;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MeiTuanReserveCallback {

    @Resource
    private StoreInfoService storeInfoService;
    @Resource
    private AppOrderService orderService;

    public CommonResult matchMethod(MeiTuanReserveReqVo reqVo) throws JsonProcessingException {
        PushEnum pushEnum = PushEnum.fromValue(reqVo.getMsgType());
        if (pushEnum == null) {
            return CommonResult.error(-200, "未找到相关枚举");
        }

        switch (pushEnum) {
            case ROOM_INFORMATION:
                log.info("---------------查询三方房间信息---------------");
                return storeInfoService.getStoreRoomInfo(reqVo.getStoreId());
            case START_BOOKING:
                log.info("---------------开始预订---------------");
                return orderService.startBooking(reqVo);
            case BOOKING_RESULT:
                log.info("---------------预订结果同步---------------");
                return CommonResult.success("");
            case CANCEL_RESERVATION:
                log.info("---------------取消预订---------------");
                return CommonResult.success("");
            case BOOKING_VERIFICATION:
                log.info("---------------预订核销同步---------------");
                return CommonResult.success("");
            case CANCEL_BOOKING_REVIEW:
                log.info("---------------取消预订审核---------------");
                return CommonResult.success("");
            case WITHDRAW_REFUND:
                log.info("---------------用户撤销退款通知---------------");
                return CommonResult.success("");
            case CHANGE_RESULT:
                log.info("---------------改约结果通知三方---------------");
                return CommonResult.success("");
            case NOTIFY_VERIFICATION:
                log.info("---------------通知三方核销---------------");
                return CommonResult.success("");
            case PREMIUM_PAYMENT:
                log.info("---------------补价支付结果---------------");
                return CommonResult.success("");
            case MULTI_COMMODITY_INVENTORY_BATCH:
                log.info("---------------多商品库存批量查询---------------");
                return CommonResult.success("");
            case GET_INVENTORY_IN_BULK:
                log.info("---------------批量获取库存---------------");
                return CommonResult.success("");
            case VERIFICATION_STATUS_QUERY:
                log.info("---------------核销状态查询---------------");
                return CommonResult.success("");
            case SINGLE_QUERY_INVENTORY:
                log.info("---------------单个查询库存---------------");
                return CommonResult.success("");
            case CHANGE_SUBMISSION:
                log.info("---------------改约提交---------------");
                return CommonResult.success("");
            case FIXED_TIME_POINT_SERVICE_PERSONNEL_INVENTORY:
                log.info("---------------批量查询商家固定时间点服务人员库存---------------");
                return CommonResult.success("");
            case HOME_INDUSTRY_INVENTORY_INQUIRY:
                log.info("---------------到家行业库存查询---------------");
                return CommonResult.success("");
        }
        return CommonResult.success("");
    }

}
