package com.yanzu.module.member.dal.mysql.usermoneybill;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.usermoneybill.vo.*;

/**
 * 用户账单明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface UserMoneyBillMapper extends BaseMapperX<UserMoneyBillDO> {

    default PageResult<UserMoneyBillDO> selectPage(UserMoneyBillPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UserMoneyBillDO>()
                .eqIfPresent(UserMoneyBillDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserMoneyBillDO::getType, reqVO.getType())
//                .betweenIfPresent(UserMoneyBillDO::getMoney, reqVO.getMoney())
                .eqIfPresent(UserMoneyBillDO::getMoneyType, reqVO.getMoneyType())
//                .betweenIfPresent(UserMoneyBillDO::getTotalMoney, reqVO.getTotalMoney())
//                .betweenIfPresent(UserMoneyBillDO::getTotalGiftMoney, reqVO.getTotalGiftMoney())
                .likeIfPresent(UserMoneyBillDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(UserMoneyBillDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserMoneyBillDO::getId));
    }

    default List<UserMoneyBillDO> selectList(UserMoneyBillExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<UserMoneyBillDO>()
                .eqIfPresent(UserMoneyBillDO::getUserId, reqVO.getUserId())
                .eqIfPresent(UserMoneyBillDO::getType, reqVO.getType())
//                .betweenIfPresent(UserMoneyBillDO::getMoney, reqVO.getMoney())
                .eqIfPresent(UserMoneyBillDO::getMoneyType, reqVO.getMoneyType())
//                .betweenIfPresent(UserMoneyBillDO::getTotalMoney, reqVO.getTotalMoney())
//                .betweenIfPresent(UserMoneyBillDO::getTotalGiftMoney, reqVO.getTotalGiftMoney())
                .likeIfPresent(UserMoneyBillDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(UserMoneyBillDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(UserMoneyBillDO::getId));
    }

}
