package com.yanzu.module.member.service.meituanreserve;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.callback.common.MeituanYudingMsgCallbackCommonRespVo;
import com.yanzu.module.member.controller.app.meituanreserve.vo.MeiTuanReserveReqVo;
import com.yanzu.module.member.service.meituanreserve.enums.PushEnum;
import com.yanzu.module.member.service.order.AppOrderService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Slf4j
public class MeiTuanReserveCallback {

    @Resource
    private StoreInfoService storeInfoService;
    @Resource
    private AppOrderService orderService;

    public void matchMethod(JSONObject jsonObject, HttpServletResponse response){
        response.setContentType("application/json; charset=utf-8");

        Integer msgType = jsonObject.getInteger("msgType");
        Long storeId = jsonObject.getLong("storeId");
        String message = jsonObject.getString("message");
        PushEnum pushEnum = PushEnum.fromValue(msgType);
        if (pushEnum == null) {
            try {
                response.getWriter().write(JSONObject.toJSONString(MeituanYudingMsgCallbackCommonRespVo.error("未找到相关枚举")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        switch (pushEnum) {
            case ROOM_INFORMATION:
                log.info("---------------查询三方房间信息---------------");
                storeInfoService.getStoreRoomInfo(storeId,response);
                break;
            case START_BOOKING:
                log.info("---------------开始预订---------------");
                orderService.startBooking(storeId,message,response);
                break;
            case BOOKING_RESULT:
                log.info("---------------预订结果同步---------------");
                orderService.resultSynchronization(storeId,message,response);
                break;
            case CANCEL_RESERVATION:
                log.info("---------------取消预订---------------");
                orderService.cancelReserve(storeId,message,response);
                break;
            case VERIFICATION_STATUS_QUERY:
                log.info("---------------核销状态查询---------------");
                orderService.verificationStatus(storeId,message,response);
                break;
            case BOOKING_VERIFICATION:
                log.info("---------------预订核销同步---------------");
                break;
            case CANCEL_BOOKING_REVIEW:
                log.info("---------------取消预订审核---------------");
                break;
            case WITHDRAW_REFUND:
                log.info("---------------用户撤销退款通知---------------");
                break;
            case CHANGE_RESULT:
                log.info("---------------改约结果通知三方---------------");
                break;
            case NOTIFY_VERIFICATION:
                log.info("---------------通知三方核销---------------");
                break;
            case PREMIUM_PAYMENT:
                log.info("---------------补价支付结果---------------");
                break;
            case MULTI_COMMODITY_INVENTORY_BATCH:
                log.info("---------------多商品库存批量查询---------------");
                break;
            case GET_INVENTORY_IN_BULK:
                log.info("---------------批量获取库存---------------");
                break;
            case SINGLE_QUERY_INVENTORY:
                log.info("---------------单个查询库存---------------");
                break;
            case CHANGE_SUBMISSION:
                log.info("---------------改约提交---------------");
                break;
            case FIXED_TIME_POINT_SERVICE_PERSONNEL_INVENTORY:
                log.info("---------------批量查询商家固定时间点服务人员库存---------------");
                break;
            case HOME_INDUSTRY_INVENTORY_INQUIRY:
                log.info("---------------到家行业库存查询---------------");
                break;
        }
    }

}
