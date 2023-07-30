package com.yanzu.module.member.dal.mysql.usermoneybill;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillRespVO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户账单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMoneyBillMapper extends BaseMapperX<UserMoneyBillDO> {


    List<AppUserMoneyBillRespVO> getOrderPage(AppUserMoneyBillPageReqVO reqVO);

    List<UserMoneyBillDO> getPayByOrderNo(@Param("orderNo") String orderNo, @Param("userId") Long userId);

}
