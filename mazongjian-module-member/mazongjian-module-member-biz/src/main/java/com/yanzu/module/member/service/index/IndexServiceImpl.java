package com.yanzu.module.member.service.index;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.index.vo.*;
import com.yanzu.module.member.controller.app.order.vo.TimeSlotVO;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import com.yanzu.module.member.dal.mysql.bannerinfo.BannerInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeinfo.StoreInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public AppIndexStoreInfoRespVO getStoreInfo(Long storeId) {
        return storeInfoMapper.getStoreInfo(storeId);
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
    public List<AppRoomInfoListRespVO> getRoomInfoList(Long storeId) {
        List<AppRoomInfoListRespVO> roomInfoList = storeInfoMapper.getRoomInfoList(storeId);
        if (!CollectionUtils.isEmpty(roomInfoList)) {
            //找出所有房间的订单
            List<OrderInfoDO> orderList = orderInfoMapper.getByRoomIds(roomInfoList.stream().map(x -> x.getRoomId()).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(orderList)) {
                //把订单按照房间id分组
                Map<Long, List<OrderInfoDO>> collect = orderList.stream().collect(Collectors.groupingBy(x -> x.getRoomId()));
                for (AppRoomInfoListRespVO respVO : roomInfoList) {
                    if (collect.containsKey(respVO.getRoomId())) {
                        respVO.setDisabledTimeSlot(collect.get(respVO.getRoomId()).stream().map(v -> new TimeSlotVO(v.getStartTime(), v.getEndTime())).collect(Collectors.toList()));
                        respVO.setStartTime(respVO.getDisabledTimeSlot().get(0).getStartTime());
                        respVO.setEndTime(respVO.getDisabledTimeSlot().get(0).getEndTime());
                    }
                }
            }
        }
        return roomInfoList;
    }
}
