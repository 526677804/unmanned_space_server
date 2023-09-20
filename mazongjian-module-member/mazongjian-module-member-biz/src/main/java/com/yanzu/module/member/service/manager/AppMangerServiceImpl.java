package com.yanzu.module.member.service.manager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.core.KeyValue;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.chart.vo.AppBusinessStatisticsRespVO;
import com.yanzu.module.member.controller.app.chart.vo.AppChartDataReqVO;
import com.yanzu.module.member.controller.app.chart.vo.AppRevenueChartRespVO;
import com.yanzu.module.member.controller.app.manager.vo.*;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import com.yanzu.module.member.dal.mysql.clearbill.ClearBillMapper;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.AppUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.dal.mysql.userwithdrawal.UserWithdrawalMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Service
@Validated
public class AppMangerServiceImpl implements AppMangerService {

    @Resource
    private ClearInfoMapper clearInfoMapper;
    @Resource
    private ClearBillMapper clearBillMapper;
    @Resource
    private StoreUserMapper storeUserMapper;

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private UserWithdrawalMapper withdrawalMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;

    @Resource
    private UserMoneyBillMapper userMoneyBillMapper;

    @Resource
    private RoomInfoMapper roomInfoMapper;

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Resource
    private StoreInfoService storeInfoService;

    @Override
    public PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        String storeIds = storeUserMapper.getIdsByUserId(getLoginUserId()).stream().collect(Collectors.joining(","));
        reqVO.setStoreIds(storeIds);
        PageHelper.startPage(reqVO);
        List<OrderListRespVO> list = orderInfoMapper.getOrderPage(reqVO);
        PageInfo<OrderListRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public PageResult<AppMemberPageRespVO> getMemberPage(AppMemberPageReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //参数检查 排序字段和排序规则  要么全部为空，要么都不能为空
        if (ObjectUtils.isEmpty(reqVO.getCloumnName()) && ObjectUtils.isEmpty(reqVO.getSortRule())) {

        } else if (!ObjectUtils.isEmpty(reqVO.getCloumnName()) && !ObjectUtils.isEmpty(reqVO.getSortRule())) {

        } else {
            throw exception(MEMBER_PAGE_PARAM_ERROR);
        }
        //内容检查
        if (!ObjectUtils.isEmpty(reqVO.getCloumnName())) {
            if (reqVO.getCloumnName().equals("createTime") || reqVO.getCloumnName().equals("orderTime") || reqVO.getCloumnName().equals("orderCount")) {
                //合法
            } else {
                throw exception(MEMBER_PAGE_PARAM_ERROR);
            }
            if (reqVO.getSortRule().toLowerCase().equals("asc") || reqVO.getSortRule().toLowerCase().equals("desc")) {
                //合法
            } else {
                throw exception(MEMBER_PAGE_PARAM_ERROR);
            }
        }
        PageHelper.startPage(reqVO);
        List<AppMemberPageRespVO> list = appUserMapper.getMemberPage(reqVO);
        PageInfo<AppMemberPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public PageResult<AppCouponPageRespVO> getPresentCouponPage(AppPresentCouponPageReqVO reqVO) {

        return null;
    }

    @Override
    public PageResult<AppCouponPageRespVO> getCouponPage(AppManagerCouponPageReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        //仅限查看当前用户所在门店的优惠券列表
        String storeIds = storeUserMapper.getIdsByUserId(getLoginUserId()).stream().collect(Collectors.joining("|"));
        PageHelper.startPage(reqVO.getPageNo(), reqVO.getPageSize());
        List<AppCouponPageRespVO> list = couponInfoMapper.getCouponPageByAdmin(reqVO, "," + storeIds + ",");
        PageInfo<AppCouponPageRespVO> page = new PageInfo<>(list);
        //再查一下门店名称
        if (!CollectionUtils.isEmpty(page.getList())) {
            Set<String> storeIdSet = Arrays.stream(page.getList().stream().map(x -> x.getStoreIds()).collect(Collectors.joining(",")).split(",")).collect(Collectors.toSet());
            List<KeyValue<Long, String>> storeNameInfo = storeInfoService.getNameMapByIds(storeIdSet);
            Map<String, String> storeNameMap = new HashMap<>(storeNameInfo.size());
            storeNameInfo.forEach(x -> storeNameMap.put(String.valueOf(x.getKey()), x.getValue()));
            //依次设置名称
            page.getList().stream().forEach(x -> {
                x.setStoreName(Arrays.stream(x.getStoreIds().split(",")).map(y -> storeNameMap.get(y)).collect(Collectors.joining(",")));
            });
        }
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public AppCouponDetailRespVO getCouponDetail(Long couponId) {
        AppCouponDetailRespVO couponDetail = couponInfoMapper.getCouponDetail(couponId);
        if (!ObjectUtils.isEmpty(couponDetail)) {
            Set<String> collect = Arrays.stream(couponDetail.getStoreIds().split(",")).collect(Collectors.toSet());
            List<KeyValue<Long, String>> storeNameInfo = storeInfoService.getNameMapByIds(collect);
            Map<String, String> storeNameMap = new HashMap<>(storeNameInfo.size());
            storeNameInfo.forEach(x -> storeNameMap.put(String.valueOf(x.getKey()), x.getValue()));
            couponDetail.setStoreName(Arrays.stream(couponDetail.getStoreIds().split(",")).map(y -> storeNameMap.get(y)).collect(Collectors.joining(",")));
        }
        return couponDetail;
    }

    @Override
    @Transactional
    public void saveCouponDetail(AppCouponDetailReqVO reqVO) {
        Long loginUserId = getLoginUserId();
        //检查包间权限
        List<String> storeIds = storeUserMapper.getIdsByUserId(loginUserId);
        if (ObjectUtils.isEmpty(reqVO.getStoreIds())) {
            reqVO.setStoreIds(storeIds.stream().collect(Collectors.joining(",")));
        } else {
            String[] split = reqVO.getStoreIds().split(",");
            for (String s : split) {
                if (!storeIds.contains(s)) {
                    throw exception(CHECK_STORE_PROMISSION_ERROR);
                }
            }
        }
        //保存进去
        CouponInfoDO couponInfoDO = new CouponInfoDO();
        couponInfoDO.setCouponName(reqVO.getCouponName());
        couponInfoDO.setType(reqVO.getType());
        couponInfoDO.setCreateUserId(loginUserId);
        couponInfoDO.setPrice(reqVO.getPrice());
        couponInfoDO.setMinUsePrice(reqVO.getMinUsePrice());
        couponInfoDO.setStoreIds(reqVO.getStoreIds());
        couponInfoDO.setExpriceTime(reqVO.getExpriceTime());
//        couponInfoDO.setRoomType(reqVO.getRoomType());
        couponInfoMapper.insert(couponInfoDO);
    }

    @Override
    public PageResult<AppClearUserPageRespVO> getClearUserPage(AppClearUserPageReqVO reqVO) {
        String ids = "";
        if (ObjectUtils.isEmpty(reqVO.getStoreId())) {
            //查询该账号权限下的所有保洁员
            List<String> storeIds = storeUserMapper.getIdsByUserId(getLoginUserId());
            ids = storeIds.stream().collect(Collectors.joining(","));
        } else {
            ids = String.valueOf(reqVO.getStoreId());
        }
        PageHelper.startPage(reqVO);
        List<AppClearUserPageRespVO> list = storeUserMapper.getClearUserPage(ids);
        PageInfo<AppClearUserPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    @Transactional
    public void deleteClearUser(Long storeId, Long userId) {
        //权限检查
        StoreUserDO do2 = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), storeId);
        if (!ObjectUtils.isEmpty(do2) && (do2.getType().intValue() == AppEnum.member_user_type.BOSS.getValue()
                || do2.getType().intValue() == AppEnum.member_user_type.ADMIN.getValue())) {
            //检查该用户在这个门店，有没有未结算的任务
            List<ClearInfoDO> clearInfoDOS = clearInfoMapper.getByUserIdAndStatusAndStoreId(userId, AppEnum.clear_info_status.FINISH.getValue(), storeId);
            if (CollectionUtils.isEmpty(clearInfoDOS)) {
                storeUserMapper.deleteClearUserAndStoreId(userId, storeId);
            } else {
                throw exception(CLEAR_USER_DELETE_ERROR);
            }
        } else {
            throw exception(AUTH_PROMISSION_ERROR);
        }
    }

    @Override
    @Transactional
    public void saveClearUser(AppClearUserDetailReqVO reqVO) {
        //通过该手机号，查询出用户
        MemberUserDO memberUserDO = memberUserMapper.selectByMobile(reqVO.getMobile());
        if (ObjectUtils.isEmpty(memberUserDO)) {
            throw exception(AUTH_USER_PHONE_ERROR);
        }
        if (memberUserDO.getId().compareTo(getLoginUserId()) == 0) {
            throw exception(OPRATION_ERROR);
        }
        //只能把用户角色改成保洁员  不能同时拥有2个类型
        if (memberUserDO.getUserType().compareTo(AppEnum.member_user_type.MEMBER.getValue()) != 0) {
            throw exception(USER_TYPE_CHECK_ERROR);
        }
        //已经绑定的门店不能再绑定
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(memberUserDO.getId(), reqVO.getStoreId());
        if (ObjectUtils.isEmpty(storeUserDO)) {
            //权限检查
            StoreUserDO do2 = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), reqVO.getStoreId());
            if (!ObjectUtils.isEmpty(do2) && (do2.getType().intValue() == AppEnum.member_user_type.BOSS.getValue()
                    || do2.getType().intValue() == AppEnum.member_user_type.ADMIN.getValue())) {
                //新增
                storeUserDO = new StoreUserDO();
                storeUserDO.setUserId(memberUserDO.getId());
                storeUserDO.setType(AppEnum.member_user_type.CLEAR.getValue());
                storeUserDO.setStoreId(reqVO.getStoreId());
                storeUserDO.setName(reqVO.getName());
                storeUserMapper.insert(storeUserDO);
                //如果用户之前不是保洁员角色 则改成保洁员的用户类型
                if (memberUserDO.getUserType().compareTo(AppEnum.member_user_type.CLEAR.getValue()) != 0) {
                    memberUserDO.setUserType(AppEnum.member_user_type.CLEAR.getValue());
                    memberUserMapper.updateById(memberUserDO);
                }
            } else {
                throw exception(AUTH_PROMISSION_ERROR);
            }
        } else {
            throw exception(DATA_EXISTS_ERROR);
        }
    }

    @Override
    @Transactional
    public void settlementClearUser(AppSettlementClearUserReqVO reqVO) {
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());

        //查询出本用户的门店权限
        String storeIds = storeUserMapper.getIdsByUserId(getLoginUserId()).stream().collect(Collectors.joining(","));
        //查询出保洁员在这些门店下   有没有可结算的订单
        List<ClearInfoDO> list = clearInfoMapper.getByUserIdAndStatusAndStoreIds(reqVO.getUserId(), AppEnum.clear_info_status.FINISH.getValue(), storeIds);
        if (CollectionUtils.isEmpty(list)) {
            throw exception(NOT_FINISH_CLEAR_IONF_ERROR);
        }
        //有订单 则结算
        list.forEach(x -> {
            x.setStatus(AppEnum.clear_info_status.JIESUAN.getValue());
            x.setSettlementTime(LocalDateTime.now());
        });
        clearInfoMapper.updateBatch(list);
        //增加结算记录
        ClearBillDO clearBillDO = new ClearBillDO();
        clearBillDO.setUserId(reqVO.getUserId());
        clearBillDO.setMoney(reqVO.getMoney());
        clearBillDO.setStoreId(reqVO.getStoreId());
        clearBillDO.setOrderNum(list.size());
        clearBillDO.setOrderIds(list.stream().map(x -> x.getOrderId().toString()).collect(Collectors.joining(",")));
        clearBillMapper.insert(clearBillDO);
    }

    @Override
    @Transactional
    public void complaintClearInfo(AppComplaintClearInfoReqVO reqVO) {
        //权限校验：管理员和加盟商允许
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());

        //先找出来
        ClearInfoDO clearInfoDO = clearInfoMapper.selectById(reqVO.getClearId());
        //只能操作已完成或已驳回的记录
        if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.TOUSU.getValue()) == 0) {
            clearInfoDO.setStatus(AppEnum.clear_info_status.FINISH.getValue());
            clearInfoDO.setComplaintDesc("管理员已撤销投诉");
            clearInfoMapper.updateById(clearInfoDO);
        } else if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.FINISH.getValue()) == 0) {
            clearInfoDO.setStatus(AppEnum.clear_info_status.TOUSU.getValue());
            clearInfoDO.setComplaintDesc(reqVO.getComplaintDesc());
            clearInfoMapper.updateById(clearInfoDO);
        } else {
            throw exception(CLEAR_INFO_STATUS_OPRATION_ERROR);
        }
    }

    private String getWithdrawalNo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDateTime currentDateTime = LocalDateTime.now();
        String currentDate = currentDateTime.format(dateFormatter);
        Random random = new Random();
        int randomNum = random.nextInt(1000000);
        String randomNumString = String.format("%04d", randomNum);
        return currentDate + randomNumString;
    }

    @Override
    @Transactional
    public void applyWithdrawal() {
        //仅创建者使用
        // 校验用户类型
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());

        //判断当前用户的收入还有没有可以提现的
        Long loginUserId = getLoginUserId();
        MemberUserDO memberUserDO = memberUserMapper.selectById(loginUserId);
        BigDecimal subtract = memberUserDO.getMoney().subtract(memberUserDO.getWithdrawalMoney());
        if (subtract.compareTo(BigDecimal.ZERO) > 0) {
            //可以提现
            //增加提现记录
            UserWithdrawalDO userWithdrawalDO = new UserWithdrawalDO();
            userWithdrawalDO.setUserId(loginUserId);
            userWithdrawalDO.setMoney(memberUserDO.getMoney());
            userWithdrawalDO.setNo(getWithdrawalNo());
            userWithdrawalDO.setStatus(AppEnum.user_withdrawal.COMMIT.getValue());
            withdrawalMapper.insert(userWithdrawalDO);
            //增加累积提现金额  并扣掉收入
            memberUserDO.setWithdrawalMoney(memberUserDO.getWithdrawalMoney().add(memberUserDO.getMoney()));
            memberUserDO.setMoney(BigDecimal.ZERO);
            memberUserMapper.updateById(memberUserDO);
            //todo..微信转账
        } else {
            //没有可提现的收入
            throw exception(USER_NO_MONEY_WITHDRAWAL_ERROR);
        }
    }

    @Override
    public PageResult<AppWithdrawalPageRespVO> getWithdrawalPage(AppWithdrawalPageReqVO reqVO) {
        //仅创建者使用
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.BOSS.getValue());
        reqVO.setUserId(getLoginUserId());
        PageHelper.startPage(reqVO);
        List<AppWithdrawalPageRespVO> list = withdrawalMapper.getWithdrawalPage(reqVO);
        PageInfo<AppWithdrawalPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public AppRevenueChartRespVO getRevenueChart() {
        //仅管理员使用
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        MemberUserDO memberUserDO = memberUserMapper.selectById(getLoginUserId());
        AppRevenueChartRespVO respVO = new AppRevenueChartRespVO();
        respVO.setMoney(memberUserDO.getMoney());
        respVO.setWithdrawalMoney(memberUserDO.getWithdrawalMoney());
        respVO.setTotalMoney(memberUserDO.getMoney().add(memberUserDO.getWithdrawalMoney()));
        return respVO;
    }

    @Override
    public AppBusinessStatisticsRespVO getBusinessStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getBusinessStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, BigDecimal>> getRevenueStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getRevenueStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, Integer>> getOrderStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getOrderStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, Integer>> getMemberStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getMemberStatistics(reqVO);
    }


    @Override
    public List<KeyValue<String, Double>> getRoomUseStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        List<KeyValue<String, Long>> roomUseStatistics = orderInfoMapper.getRoomUseStatistics(reqVO);
        List<KeyValue<String, Double>> resultList = new ArrayList<>();
        //再查一下总共的房间数量，算出使用率
        if (!CollectionUtils.isEmpty(roomUseStatistics)) {
            Integer count = roomInfoMapper.countByStoreIdAndUserId(reqVO.getStoreId(), reqVO.getUserId());
            for (KeyValue<String, Long> vo : roomUseStatistics) {
//                vo.setValue(vo.getValue() / (count * 1.0));
                KeyValue<String, Double> kv = new KeyValue();
                kv.setKey(vo.getKey());
                kv.setValue(vo.getValue().doubleValue() / count);
                resultList.add(kv);
            }
        }
        return resultList;
    }

    @Override
    public List<KeyValue<String, Double>> getRoomUseHourStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getRoomUseHourStatistics(reqVO);
    }

    @Override
    @Transactional
    public void giftCoupon(AppGiftCouponReqVO reqVO) {
        //仅管理员使用 检查权限
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        CouponInfoDO couponInfoDO = couponInfoMapper.getByIdAndAdmin(reqVO.getCouponId());
        if (!ObjectUtils.isEmpty(couponInfoDO)) {
            CouponInfoDO newCouponInfoDO = new CouponInfoDO();
            BeanUtils.copyProperties(couponInfoDO, newCouponInfoDO);
            newCouponInfoDO.setCouponId(null);
            newCouponInfoDO.setUserId(reqVO.getUserId());
            couponInfoMapper.insert(newCouponInfoDO);
        }

    }
}
