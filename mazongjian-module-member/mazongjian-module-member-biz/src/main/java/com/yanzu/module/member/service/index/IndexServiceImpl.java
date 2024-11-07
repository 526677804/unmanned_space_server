package com.yanzu.module.member.service.index;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.module.infra.api.config.ConfigApi;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.mysql.bannerinfo.BannerInfoMapper;
import com.yanzu.module.member.dal.mysql.discountrules.DiscountRulesMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yanzu.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.service.index
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/28 9:41
 */
@Service
@Validated
public class IndexServiceImpl implements IndexService {

    public static final int SEVEN_DAY = 7;

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
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private ConfigApi configApi;

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
        Long userId = null;
        if (reqVO.isOften()) {
            userId = getLoginUserId();
        }
        if (!ObjectUtils.isEmpty(reqVO.getCityName())) {
            if (reqVO.getCityName().equals("选择城市") || reqVO.getCityName().equals("请选择")) {
                reqVO.setCityName("");
            }
        }
        IPage<AppStorePageRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        storeInfoMapper.getStorePageList(page, reqVO, userId);
        if (!CollectionUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getDistance())) {
                    x.setDistance(x.getDistance().setScale(2, BigDecimal.ROUND_CEILING));
                }
                if (x.getSubscribeTime()!= null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime subscribeDateTime = LocalDateTime.parse(x.getSubscribeTime(),formatter);
                    LocalDateTime currentDateTime = LocalDateTime.now();
                    Duration duration = Duration.between(subscribeDateTime, currentDateTime);
                    x.setSubscribeTime(String.valueOf(duration.toMinutes()));
                }
            });
        }
        return new PageResult<>(page.getRecords(), page.getTotal());
    }


    @Override
    public AppIndexStoreInfoRespVO getStoreInfo(Long storeId, String lat, String lon) {
        AppIndexStoreInfoRespVO storeInfo = storeInfoMapper.getStoreInfo(storeId, lat, lon);
        if (!ObjectUtils.isEmpty(storeInfo)) {
            storeInfo.setRoomClassList(roomInfoMapper.getClassList(storeId));
            if (!ObjectUtils.isEmpty(storeInfo.getDistance())) {
                storeInfo.setDistance(storeInfo.getDistance().setScale(2, BigDecimal.ROUND_CEILING));
            }else {
                storeInfo.setDistance(new BigDecimal(9999));
            }
            storeInfo.setDiscountRules(discountRulesMapper.getRulesByStoreId(storeId));
        }
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
    public List<AppRoomInfoListRespVO> getRoomInfoList(AppRoomListReqVO reqVO) {
        //获取所有房间信息
        List<AppRoomInfoListRespVO> roomInfoList = storeInfoMapper.getRoomInfoList(reqVO);
        if (!CollectionUtils.isEmpty(roomInfoList)) {
            //找出所有房间的订单
            List<OrderInfoDO> orderList = orderInfoMapper.getByRoomIds(roomInfoList.stream().map(x -> x.getRoomId()).collect(Collectors.toList()));
            //把订单按照房间id分组
            Map<String, List<OrderInfoDO>> orederMap;
            if (!CollectionUtils.isEmpty(orderList)) {
                orederMap = orderList.stream().collect(Collectors.groupingBy(x -> String.valueOf(x.getRoomId())));
            } else {
                orederMap = new HashMap<>();
            }
            DateTimeFormatter formatterDay = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH:mm");
            // 获取当前日期
            int hour = LocalDateTime.now().getHour();
//            LocalDate now = LocalDate.now();
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
                if (orederMap.containsKey(respVO.getRoomId().toString())) {
                    List<OrderInfoDO> sortOrder = orederMap.get(respVO.getRoomId().toString()).stream().sorted(Comparator.comparing(OrderInfoDO::getStartTime)).collect(Collectors.toList());
                    //把第一个订单的开始和结束时间 设置给房间
                    respVO.setStartTime(sortOrder.get(0).getStartTime());
                    respVO.setEndTime(sortOrder.get(0).getEndTime());
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
            //把第一个订单的信息设置给房间
            respVO.setStartTime(orderList.get(0).getStartTime());
            respVO.setEndTime(orderList.get(0).getEndTime());
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
            List<AppOrderTimeVO> orderTimeVOList=new ArrayList<>(sortOrder.size());
            sortOrder.forEach(x -> {
                bookings.add(new AppOrderTimeVO(x.getStartTime(), x.getEndTime()));
                orderTimeVOList.add(new AppOrderTimeVO(x.getStartTime(),x.getEndTime()));
            });
            respVO.setOrderTimeList(orderTimeVOList);
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

    @Override
    public AppSysInfoRespVO getSysInfo() {
        AppSysInfoRespVO respVO = new AppSysInfoRespVO();
        respVO.setVersion(configApi.getConfigValueByKey("app.version"));
        return respVO;
    }


}
