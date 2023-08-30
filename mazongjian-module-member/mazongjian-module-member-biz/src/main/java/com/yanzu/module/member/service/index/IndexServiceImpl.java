package com.yanzu.module.member.service.index;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.controller.app.order.vo.TimeRange;
import com.yanzu.module.member.controller.app.order.vo.TimeSlotVO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.mysql.bannerinfo.BannerInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
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
import java.util.*;
import java.util.stream.Collectors;

import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;

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
        PageHelper.startPage(reqVO);
        List<AppStorePageRespVO> list = storeInfoMapper.getStorePageList(reqVO);
        PageInfo<AppStorePageRespVO> page = new PageInfo<>(list);
        if (!CollectionUtils.isEmpty(page.getList())) {
            page.getList().forEach(x -> x.setDistance(x.getDistance().setScale(2, BigDecimal.ROUND_CEILING)));
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
        if (getLoginUserType().compareTo(AppEnum.member_user_type.MEMBER.getValue()) == 0
                || getLoginUserType().compareTo(2) == 0) {
            //是APP用户  或者 后台管理员  则返回全部
            return storeInfoMapper.getStoreListByMember(name, cityName);
        } else {
            return storeInfoMapper.getStoreList(name, cityName, getLoginUserId());
        }
    }

    @Override
    public List<KeyValue<String, Long>> getRoomList(Long storeId) {
        return roomInfoMapper.getRoomList(storeId, getLoginUserId());
    }

    @Override
    public List<KeyValue<String, Long>> getRoomListByAdmin(Long storeId) {
        return roomInfoMapper.getRoomListByAdmin(storeId);
    }

    @Override
    public List<AppRoomInfoListRespVO> getRoomInfoList(Long storeId) {
        //获取所有房间信息
        List<AppRoomInfoListRespVO> roomInfoList = storeInfoMapper.getRoomInfoList(storeId);
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
            LocalDate currentDate = LocalDate.now();
            Set<String> days = new HashSet<>(5);
            for (int i = 0; i < 5; i++) {
                days.add(currentDate.format(formatterDay));
                currentDate = currentDate.plusDays(1);
            }
            //开始处理
            for (AppRoomInfoListRespVO respVO : roomInfoList) {
                List<TimeRange> disabledTimeRanges = new ArrayList<>();
                //先把订单中的时间进行处理
                if (orederMap.containsKey(respVO.getRoomId())) {
                    respVO.setStartTime(orederMap.get(respVO.getRoomId()).get(0).getStartTime());
                    respVO.setEndTime(orederMap.get(respVO.getRoomId()).get(0).getEndTime());
                    for (OrderInfoDO orderInfoDO : orederMap.get(respVO.getRoomId())) {
                        if (orderInfoDO.getStartTime().getDay() != orderInfoDO.getEndTime().getDay()) {
                            //开始时间与结束时间跨日 分隔成两段
                            LocalDateTime start = DateUtils.of(orderInfoDO.getStartTime());
                            LocalDateTime end = DateUtils.of(orderInfoDO.getEndTime());
                            disabledTimeRanges.add(new TimeRange(start, start.with(LocalTime.of(23, 59))));
                            disabledTimeRanges.add(new TimeRange(end.with(LocalTime.of(00, 00)), end));
                        } else {
                            disabledTimeRanges.add(new TimeRange(DateUtils.of(orderInfoDO.getStartTime()), DateUtils.of(orderInfoDO.getEndTime())));
                        }
                    }
                }
                //再处理每天有禁用时间的情况
                if (!ObjectUtils.isEmpty(respVO.getBanTimeStart()) && !ObjectUtils.isEmpty(respVO.getBanTimeStart())) {
                    // 获取当前日期
                    currentDate = LocalDate.now();
                    // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
                    LocalTime bstart = LocalTime.parse(respVO.getBanTimeStart());
                    LocalTime bend = LocalTime.parse(respVO.getBanTimeEnd());
                    // 遍历日期范围内的每一天'
                    for (int i = 0; i < 5; i++) {
                        // 判断是否跨越两天
                        if (bend.isBefore(bstart)) {
                            // 添加禁用时间范围：从开始时间到当天最后一秒
                            disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(LocalTime.MAX)));
                            // 添加禁用时间范围：从零点到结束时间
                            disabledTimeRanges.add(new TimeRange(currentDate.atTime(LocalTime.MIDNIGHT), currentDate.atTime(bend)));
                        } else {
                            // 添加禁用时间范围：从开始时间到结束时间
                            disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(bend)));
                        }
                        currentDate = currentDate.plusDays(1);
                    }
                }
                //排序结果，并按日期进行分组
                Map<String, List<TimeRange>> collect = disabledTimeRanges.stream().sorted(Comparator.comparing(TimeRange::getStart))
                        .collect(Collectors.groupingBy(v -> v.getStart().format(formatterDay)));
                Map<String, List<TimeSlotVO>> timeSlotMap = new HashMap<>(collect.size());
                for (Map.Entry<String, List<TimeRange>> entry : collect.entrySet()) {
                    List<TimeSlotVO> timeSlotVOList = new ArrayList<>(entry.getValue().size());
                    for (TimeRange timeRange : entry.getValue()) {
                        timeSlotVOList.add(new TimeSlotVO(timeRange.getStart().format(formatterHour), timeRange.getEnd().format(formatterHour)));
                    }
                    timeSlotMap.put(entry.getKey(), timeSlotVOList);
                }
                days.forEach(d -> {
                    if (!timeSlotMap.containsKey(d)) {
                        timeSlotMap.put(d, new ArrayList<>(1));
                    }
                });
                respVO.setDisabledTimeSlot(timeSlotMap);
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

        DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        Set<String> days = new HashSet<>(5);
        for (int i = 0; i < 5; i++) {
            days.add(currentDate.format(formatterDay));
            currentDate = currentDate.plusDays(1);
        }
        //开始处理
        List<TimeRange> disabledTimeRanges = new ArrayList<>();
        //先把订单中的时间进行处理
        if (orederMap.containsKey(respVO.getRoomId())) {
            respVO.setStartTime(orederMap.get(respVO.getRoomId()).get(0).getStartTime());
            respVO.setEndTime(orederMap.get(respVO.getRoomId()).get(0).getEndTime());
            for (OrderInfoDO orderInfoDO : orederMap.get(respVO.getRoomId())) {
                if (orderInfoDO.getStartTime().getDay() != orderInfoDO.getEndTime().getDay()) {
                    //开始时间与结束时间跨日 分隔成两段
                    LocalDateTime start = DateUtils.of(orderInfoDO.getStartTime());
                    LocalDateTime end = DateUtils.of(orderInfoDO.getEndTime());
                    disabledTimeRanges.add(new TimeRange(start, start.with(LocalTime.of(23, 59))));
                    disabledTimeRanges.add(new TimeRange(end.with(LocalTime.of(00, 00)), end));
                } else {
                    disabledTimeRanges.add(new TimeRange(DateUtils.of(orderInfoDO.getStartTime()), DateUtils.of(orderInfoDO.getEndTime())));
                }
            }
        }
        //再处理每天有禁用时间的情况
        if (!ObjectUtils.isEmpty(respVO.getBanTimeStart()) && !ObjectUtils.isEmpty(respVO.getBanTimeStart())) {
            // 获取当前日期
            currentDate = LocalDate.now();
            // 禁用时间段列表，包含禁用开始时间和结束时间 new TimeRange("02:00", "08:00")
            LocalTime bstart = LocalTime.parse(respVO.getBanTimeStart());
            LocalTime bend = LocalTime.parse(respVO.getBanTimeEnd());
            // 遍历日期范围内的每一天'
            for (int i = 0; i < 5; i++) {
                // 判断是否跨越两天
                if (bend.isBefore(bstart)) {
                    // 添加禁用时间范围：从开始时间到当天最后一秒
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(LocalTime.MAX)));
                    // 添加禁用时间范围：从零点到结束时间
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(LocalTime.MIDNIGHT), currentDate.atTime(bend)));
                } else {
                    // 添加禁用时间范围：从开始时间到结束时间
                    disabledTimeRanges.add(new TimeRange(currentDate.atTime(bstart), currentDate.atTime(bend)));
                }
                currentDate = currentDate.plusDays(1);
            }
        }
        //排序结果，并按日期进行分组
        Map<String, List<TimeRange>> collect = disabledTimeRanges.stream().sorted(Comparator.comparing(TimeRange::getStart))
                .collect(Collectors.groupingBy(v -> v.getStart().format(formatterDay)));
        Map<String, List<TimeSlotVO>> timeSlotMap = new HashMap<>(collect.size());
        for (Map.Entry<String, List<TimeRange>> entry : collect.entrySet()) {
            List<TimeSlotVO> timeSlotVOList = new ArrayList<>(entry.getValue().size());
            for (TimeRange timeRange : entry.getValue()) {
                timeSlotVOList.add(new TimeSlotVO(timeRange.getStart().format(formatterHour), timeRange.getEnd().format(formatterHour)));
            }
            timeSlotMap.put(entry.getKey(), timeSlotVOList);
        }
        days.forEach(d -> {
            if (!timeSlotMap.containsKey(d)) {
                timeSlotMap.put(d, new ArrayList<>(1));
            }
        });
        respVO.setDisabledTimeSlot(timeSlotMap);
        return respVO;
    }


}
