package com.yanzu.module.member.service.meituanreserve;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.meituanreserve.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.dataobject.roominfo.RoomInfoDO;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.forest.MeiTuanReserveClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MeiTuanReserveService {

    private static final String CLIENT_ID = "71b71240-f15f";
    private static final String SECRET = "b92c9bdb-8b50-4454-b588-5e66a5e858fd";

    @Autowired
    private MeiTuanReserveClient reserveClient;

    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private OrderInfoMapper orderInfoMapper;

    /**
     * 推送规则
     *
     * @param reqVo
     * @return
     */
    public CommonResult pushRule(StoreRuleReqVo reqVo) {
        StoreRulePushReqVo pushReqVo = BeanUtil.toBean(reqVo, StoreRulePushReqVo.class);
        pushReqVo.setStoreId(6);
        return reserveClient.pushRule(pushReqVo, CLIENT_ID, SECRET);
    }

    /**
     * 产生订单后、修改房间信息调用 主动推送房间信息至美团
     *
     * @param roomId
     * @return
     */
    public CommonResult updateStock(Long roomId) {

        // updateStockReqVos 发送client请求的vo
        List<UpdateStockReqVo> updateStockReqVos = new ArrayList<>();

        RoomInfoDO roomInfoDO = roomInfoMapper.selectById(roomId);

        UpdateStockReqVo updateStockReqVo = new UpdateStockReqVo();
        updateStockReqVo.setThirdPartyRoomId(roomInfoDO.getRoomId());
        updateStockReqVo.setRoomName(roomInfoDO.getRoomName());
        updateStockReqVo.setStoreId(roomInfoDO.getStoreId());

        List<DeskSoldTimePeriodsSub> deskSoldTimePeriodsSubs = new ArrayList<>();
        DeskSoldTimePeriodsSub deskSoldTimePeriodsSub = new DeskSoldTimePeriodsSub();
        // 设置业务类型，1-普通预订，2-包座，3-押金预订
        // 房间设置时 判断是否设置了押金 有押金则属于押金预定
        deskSoldTimePeriodsSub.setBizType(roomInfoDO.getDeposit().compareTo(BigDecimal.ZERO) == 0 ? "1" : "3");
        List<TimePeriodItemsSub> timePeriodItemsSubs = new ArrayList<>();
        TimePeriodItemsSub timePeriodItemsSub = new TimePeriodItemsSub();
        // 房间自带的禁止时间  todo 是否应该查询当天的订单 将开始和结束时间设置上去？
        if (ObjectUtils.isEmpty(roomInfoDO.getBanTimeStart())) {
            timePeriodItemsSub.setBeginMinutes(0);
            timePeriodItemsSub.setEndMinutes(0);
            timePeriodItemsSub.setBeginTime(0L);
            timePeriodItemsSub.setEndTime(0L);
        } else {
            String banTimeStart = roomInfoDO.getBanTimeStart();
            String banTimeEnd = roomInfoDO.getBanTimeEnd();
            timePeriodItemsSub.setBeginMinutes(convertToMinutes(banTimeStart));
            timePeriodItemsSub.setEndMinutes(convertToMinutes(banTimeEnd));
            timePeriodItemsSub.setBeginTime(System.currentTimeMillis());
            timePeriodItemsSub.setEndTime(Long.valueOf(convertToMinutes(banTimeEnd)));
        }
        timePeriodItemsSubs.add(timePeriodItemsSub);

        deskSoldTimePeriodsSub.setTimePeriodItems(timePeriodItemsSubs);
        deskSoldTimePeriodsSubs.add(deskSoldTimePeriodsSub);
        updateStockReqVo.setDeskSoldTimePeriods(deskSoldTimePeriodsSubs);
        updateStockReqVos.add(updateStockReqVo);

        return reserveClient.pushData(updateStockReqVos, CLIENT_ID, SECRET);
    }

    public static Integer convertToMinutes(String timeStr) {
        String[] parts = timeStr.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        return hours * 60 + minutes;
    }

}
