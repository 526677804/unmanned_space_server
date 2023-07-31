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
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.dataobject.userwithdrawal.UserWithdrawalDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.dal.mysql.roominfo.RoomInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;
import com.yanzu.module.member.dal.mysql.userwithdrawal.UserWithdrawalMapper;
import com.yanzu.module.member.enums.AppEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

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

    @Override
    public PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO) {
        return null;
    }

    @Override
    public PageResult<AppMemberPageRespVO> getMemberPage(AppMemberPageReqVO reqVO) {
        return null;
    }

    @Override
    public PageResult<AppCouponPageRespVO> getPresentCouponPage(AppPresentCouponPageReqVO reqVO) {
        return null;
    }

    @Override
    public PageResult<AppCouponPageRespVO> getCouponPage(AppManagerCouponPageReqVO reqVO) {
        return null;
    }

    @Override
    public AppCouponDetailRespVO getCouponDetail(Long couponId) {
        return null;
    }

    @Override
    public void saveCouponDetail(AppCouponDetailReqVO reqVO) {

    }

    @Override
    public PageResult<AppClearUserPageRespVO> getClearUserPage(AppClearUserPageReqVO reqVO) {
        PageHelper.startPage(reqVO);
        List<AppClearUserPageRespVO> list = storeUserMapper.getClearUserPage(reqVO);
        PageInfo<AppClearUserPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    @Transactional
    public void deleteClearUser(Long storeId, Long userId) {
        //权限检查
        StoreUserDO do2 = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), storeId);
        if (!ObjectUtils.isEmpty(do2) && (do2.getType().intValue() == 1 || do2.getType().intValue() == 2)) {
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
        //已经绑定的门店不能再绑定
        StoreUserDO storeUserDO = storeUserMapper.getByUserIdAndStoreId(memberUserDO.getId(), reqVO.getStoreId());
        if (ObjectUtils.isEmpty(storeUserDO)) {
            //权限检查
            StoreUserDO do2 = storeUserMapper.getByUserIdAndStoreId(getLoginUserId(), reqVO.getStoreId());
            if (!ObjectUtils.isEmpty(do2) && (do2.getType().intValue() == 1 || do2.getType().intValue() == 2)) {
                //新增
                storeUserDO = new StoreUserDO();
                storeUserDO.setUserId(memberUserDO.getId());
                storeUserDO.setType(AppEnum.member_user_type.CLEAR.getValue());
                storeUserDO.setStoreId(reqVO.getStoreId());
                storeUserDO.setName(reqVO.getName());
                storeUserMapper.insert(storeUserDO);
            } else {
                throw exception(AUTH_PROMISSION_ERROR);
            }
        } else {
            throw exception(DATA_EXISTS_ERROR);
        }
    }

    @Override
    public void settlementClearUser(AppSettlementClearUserReqVO reqVO) {

    }

    @Override
    public void complaintClearInfo(AppComplaintClearInfoReqVO reqVO) {

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
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
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
        } else {
            //没有可提现的收入
            throw exception(USER_NO_MONEY_WITHDRAWAL_ERROR);
        }
    }

    @Override
    public PageResult<AppWithdrawalPageRespVO> getWithdrawalPage(AppWithdrawalPageReqVO reqVO) {
        //仅创建者使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        PageHelper.startPage(reqVO);
        List<AppWithdrawalPageRespVO> list = withdrawalMapper.getWithdrawalPage(reqVO);
        PageInfo<AppWithdrawalPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    public AppRevenueChartRespVO getRevenueChart() {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
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
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getBusinessStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, BigDecimal>> getRevenueStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getRevenueStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, Integer>> getOrderStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getOrderStatistics(reqVO);
    }

    @Override
    public List<KeyValue<String, Integer>> getMemberStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getMemberStatistics(reqVO);
    }


    @Override
    public List<KeyValue<String, Double>> getRoomUseStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        List<KeyValue<String, Double>> roomUseStatistics = orderInfoMapper.getRoomUseStatistics(reqVO);
        //再查一下总共的房间数量，算出使用率
        if (!CollectionUtils.isEmpty(roomUseStatistics)) {
            int count = roomInfoMapper.countByStoreIdAndUserId(reqVO.getStoreId(), reqVO.getUserId());
            for (KeyValue<String, Double> vo : roomUseStatistics) {
                vo.setValue(vo.getValue() / (count * 1.0));
            }
        }
        return roomUseStatistics;
    }

    @Override
    public List<KeyValue<String, Double>> getRoomUseHourStatistics(AppChartDataReqVO reqVO) {
        //仅管理员使用
        if (getLoginUserType().compareTo(AppEnum.member_user_type.FRANCHISEE.getValue()) != 0
                && getLoginUserType().compareTo(AppEnum.member_user_type.ADMIN.getValue()) != 0) {
            throw exception(AUTH_PROMISSION_ERROR);
        }
        reqVO.setUserId(getLoginUserId());
        return orderInfoMapper.getRoomUseHourStatistics(reqVO);
    }
}
