package com.yanzu.module.member.service.manager;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.manager.vo.*;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import com.yanzu.module.member.dal.dataobject.user.MemberUserDO;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.storeuser.StoreUserMapper;
import com.yanzu.module.member.dal.mysql.user.MemberUserMapper;
import com.yanzu.module.member.enums.AppEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
    private StoreUserMapper storeUserMapper;

    @Resource
    private MemberUserMapper memberUserMapper;

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
}
