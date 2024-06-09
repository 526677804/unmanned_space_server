package com.yanzu.module.member.dal.mysql.usermoneybill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppUserMoneyBillRespVO;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户账单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMoneyBillMapper extends BaseMapperX<UserMoneyBillDO> {


    IPage<AppUserMoneyBillRespVO> getBalancePage(@Param("page") IPage<AppUserMoneyBillRespVO> page, @Param("reqVO") AppUserMoneyBillPageReqVO reqVO);

    List<UserMoneyBillDO> getPayByOrderNo(@Param("orderNo") String orderNo, @Param("userId") Long userId);

}
