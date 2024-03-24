package com.yanzu.module.member.service.index;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.mysql.bannerinfo.BannerInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.index
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 9:41
 */
@Service
@Validated
public class IndexServiceImpl implements IndexService {

    @Resource
    private StoreInfoMapper storeInfoMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;
    @Resource
    private BannerInfoMapper bannerInfoMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private DiscountRulesMapper discountRulesMapper;

    @Override
    public List<String> getCityList() {
        return storeInfoMapper.getCityList();
    }

    @Override
    public List<AppBannerInfoRespVO> getBannerList() {
        return bannerInfoMapper.getBannerList(1);//1=首页
    }

    @Override
    public PageResult<AppStorePageRespVO> getStorePageList(AppStorePageReqVO reqVO) {
        if (!ObjectUtils.isEmpty(reqVO.getCityName())) {
            if (reqVO.getCityName().equals("选择城市") || reqVO.getCityName().equals("请选择")) {
                reqVO.setCityName("");
            }
        }

        PageHelper.startPage(reqVO);
        List<AppStorePageRespVO> list = storeInfoMapper.getStorePageList(reqVO);
        PageInfo<AppStorePageRespVO> page = new PageInfo<>(list);
        if (!CollectionUtils.isEmpty(page.getList())) {
            page.getList().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getDistance())) {
                    x.setDistance(x.getDistance().setScale(2, BigDecimal.ROUND_CEILING));
                }
            });
        }
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public AppIndexStoreInfoRespVO getStoreInfo(Long storeId) {
        AppIndexStoreInfoRespVO storeInfo = storeInfoMapper.getStoreInfo(storeId);
        storeInfo.setDiscountRules(discountRulesMapper.getRulesByStoreId(storeId));
        return storeInfo;
    }

    @Override
    public List<KeyValue<String, Long>> getStoreList(String name, String cityName) {
        return storeInfoMapper.getStoreListByMember(name, cityName);
    }

    @Override
    public List<KeyValue<String, Long>> getRoomList(Long storeId) {
        return roomInfoMapper.getRoomListByStoreId(storeId);
    }

    @Override
    public List<KeyValue<String, Long>> getRoomListByAdmin(Long storeId) {
        return roomInfoMapper.getRoomListByAdmin(storeId);
    }

    @Override
    public List<AppRoomInfoListRespVO> getRoomInfoList(Long storeId,Integer roomClass) {
        //获取所有房间信息
        List<AppRoomInfoListRespVO> roomInfoList = storeInfoMapper.getRoomInfoList(storeId,roomClass);
        if (!CollectionUtils.isEmpty(roomInfoList)) {
            //找出所有房间的订单
            List<OrderInfoDO> orderList = orderInfoMapper.getByRoomIds(roomInfoList.stream().map(x -> x.getRoomId()).collect(Collectors.toList()));
            //把订单按照房间id分组
            Map<Long, List<OrderInfoDO>> orederMap;
            if (!CollectionUtils.isEmpty(orderList)) {
                orederMap = orderList.stream().collect(Collectors.groupingBy(x -> x.getRoomId()));
            } else {
                orederMap = new HashMap<>();
            }

            DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
            // 获取当前日期
            int hour = LocalDateTime.now().getHour();
            LocalDate now = LocalDate.now();
            LocalDate currentDate = LocalDate.now();
            Set<String> days = new HashSet<>(5);
            for (int i = 0; i < 5; i++) {
                days.add(currentDate.format(formatterDay));
                currentDate = currentDate.plusDays(1);
            }
            //重新处理不可用时间段的显示
            for (AppRoomInfoListRespVO respVO : roomInfoList) {
                //一天24个 5天就是120个
                List<AppTimeSlotRespVO> timeSlot = new ArrayList<>(120);
                //暂存所有禁用的时间段
                List<AppOrderTimeVO> bookings = new ArrayList<>();
                //找出该房间所有订单
                if (orederMap.containsKey(respVO.getRoomId())) {
                    List<OrderInfoDO> sortOrder = orederMap.get(respVO.getRoomId()).stream().sorted(Comparator.comparing(OrderInfoDO::getStartTime)).collect(Collectors.toList());
                    sortOrder.forEach(x -> {
                        bookings.add(new AppOrderTimeVO(x.getStartTime(), x.getEndTime()));
                    });
                }
                //每日不可用时间
                LocalTime bstart = null;
                LocalTime bend = null;
                // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
                if (!ObjectUtils.isEmpty(respVO.getBanTimeStart()) && !ObjectUtils.isEmpty(respVO.getBanTimeStart())) {
                    bstart = LocalTime.parse(respVO.getBanTimeStart());
                    bend = LocalTime.parse(respVO.getBanTimeEnd());
                }
                // 获取从今天起未来5天的小时数
                int totalHours = 5 * 24;
                // 获取当前时间的当天0时
                LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
                // 获取每小时的可用状态
                for (int i = 0; i < totalHours; i++) {
                    LocalDateTime currentHour = currentDateTime.plusHours(i);
                    boolean isAvailable = false;
                    for (AppOrderTimeVO booking : bookings) {
                        if (booking.getStartTime().isBefore(currentHour.plusHours(1)) &&
                                booking.getEndTime().isAfter(currentHour)) {
                            isAvailable = true;
                            break;
                        }
                    }
                    if (null != bstart && null != bend) {
                        // 处理禁用时间跨越两天的情况
                        int currentHourValue = currentHour.getHour();
                        if (bstart.getHour() > bend.getHour()) {
                            if (currentHourValue >= bstart.getHour() || currentHourValue < bend.getHour()) {
                                isAvailable = true;
                            }
                        } else {
                            if (currentHourValue >= bstart.getHour() && currentHourValue < bend.getHour()) {
                                isAvailable = true;
                            }
                        }
                    }
                    // 将小时和可用状态放入结果中
                    String formattedHour = currentHour.format(DateTimeFormatter.ofPattern("HH"));
                    timeSlot.add(new AppTimeSlotRespVO(formattedHour, isAvailable));
                }
                //重新处理当日的，从当前时间的小时开始 设置到次日
                List<AppTimeSlotRespVO> appTimeSlotRespVOS = timeSlot.subList(hour, hour + 24);
                for (int i = 0; i < appTimeSlotRespVOS.size(); i++) {
                    AppTimeSlotRespVO slotRespVO = appTimeSlotRespVOS.get(i);
                    if (slotRespVO.getHour().equals("00")) {
                        slotRespVO = new AppTimeSlotRespVO("次", slotRespVO.getDisable());
                    }
                    timeSlot.set(i, slotRespVO);
                }
                respVO.setTimeSlot(timeSlot);
            }
        }
        return roomInfoList;
    }

    @Override
    public AppRoomInfoListRespVO getRoomInfo(Long roomId) {
        AppRoomInfoListRespVO respVO = storeInfoMapper.getRoomInfo(roomId);
        //找出所有房间的订单
        List<OrderInfoDO> orderList = orderInfoMapper.getByRoomId(roomId, null);
        //把订单按照房间id分组
        Map<Long, List<OrderInfoDO>> orederMap;
        if (!CollectionUtils.isEmpty(orderList)) {
            orederMap = orderList.stream().collect(Collectors.groupingBy(x -> x.getRoomId()));
        } else {
            orederMap = new HashMap<>();
        }
        // 获取当前日期
        int hour = LocalDateTime.now().getHour();
        //重新处理不可用时间段的显示
        //一天24个 5天就是120个
        List<AppTimeSlotRespVO> timeSlot = new ArrayList<>(120);
        //暂存所有禁用的时间段
        List<AppOrderTimeVO> bookings = new ArrayList<>();
        //找出该房间所有订单
        if (orederMap.containsKey(respVO.getRoomId())) {
            List<OrderInfoDO> sortOrder = orederMap.get(respVO.getRoomId()).stream().sorted(Comparator.comparing(OrderInfoDO::getStartTime)).collect(Collectors.toList());
            sortOrder.forEach(x -> {
                bookings.add(new AppOrderTimeVO(x.getStartTime(), x.getEndTime()));
            });
        }
        //每日不可用时间
        LocalTime bstart = null;
        LocalTime bend = null;
        // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
        if (!ObjectUtils.isEmpty(respVO.getBanTimeStart()) && !ObjectUtils.isEmpty(respVO.getBanTimeStart())) {
            bstart = LocalTime.parse(respVO.getBanTimeStart());
            bend = LocalTime.parse(respVO.getBanTimeEnd());
        }
        // 获取从今天起未来5天的小时数
        int totalHours = 5 * 24;
        // 获取当前时间的当天0时
        LocalDateTime currentDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        // 获取每小时的可用状态
        for (int i = 0; i < totalHours; i++) {
            LocalDateTime currentHour = currentDateTime.plusHours(i);
            boolean isAvailable = false;
            for (AppOrderTimeVO booking : bookings) {
                if (booking.getStartTime().isBefore(currentHour.plusHours(1)) &&
                        booking.getEndTime().isAfter(currentHour)) {
                    isAvailable = true;
                    break;
                }
            }
            if (null != bstart && null != bend) {
                // 处理禁用时间跨越两天的情况
                int currentHourValue = currentHour.getHour();
                if (bstart.getHour() > bend.getHour()) {
                    if (currentHourValue >= bstart.getHour() || currentHourValue < bend.getHour()) {
                        isAvailable = true;
                    }
                } else {
                    if (currentHourValue >= bstart.getHour() && currentHourValue < bend.getHour()) {
                        isAvailable = true;
                    }
                }
            }
            // 将小时和可用状态放入结果中
            String formattedHour = currentHour.format(DateTimeFormatter.ofPattern("HH"));
            timeSlot.add(new AppTimeSlotRespVO(formattedHour, isAvailable));
        }
        //重新处理当日的，从当前时间的小时开始 设置到次日
        List<AppTimeSlotRespVO> appTimeSlotRespVOS = timeSlot.subList(hour, hour + 24);
        for (int i = 0; i < appTimeSlotRespVOS.size(); i++) {
            AppTimeSlotRespVO slotRespVO = appTimeSlotRespVOS.get(i);
            if (slotRespVO.getHour().equals("00")) {
                slotRespVO = new AppTimeSlotRespVO("次", slotRespVO.getDisable());
            }
            timeSlot.set(i, slotRespVO);
        }
        respVO.setTimeSlot(timeSlot);

        return respVO;
    }


}
