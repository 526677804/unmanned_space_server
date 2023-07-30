package com.yanzu.module.member.service.manager;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.manager.vo.*;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppCouponPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;

public interface AppMangerService {
    PageResult<OrderListRespVO> getOrderPage(OrderPageReqVO reqVO);

    PageResult<AppMemberPageRespVO> getMemberPage(AppMemberPageReqVO reqVO);

    PageResult<AppCouponPageRespVO> getPresentCouponPage(AppPresentCouponPageReqVO reqVO);

    PageResult<AppCouponPageRespVO> getCouponPage(AppManagerCouponPageReqVO reqVO);

    AppCouponDetailRespVO getCouponDetail(Long couponId);

    void saveCouponDetail(AppCouponDetailReqVO reqVO);

    PageResult<AppClearUserPageRespVO> getClearUserPage(AppClearUserPageReqVO reqVO);

    void deleteClearUser(Long storeId,Long userId);

    void saveClearUser(AppClearUserDetailReqVO reqVO);

    void settlementClearUser(AppSettlementClearUserReqVO reqVO);

    void complaintClearInfo(AppComplaintClearInfoReqVO reqVO);
}
