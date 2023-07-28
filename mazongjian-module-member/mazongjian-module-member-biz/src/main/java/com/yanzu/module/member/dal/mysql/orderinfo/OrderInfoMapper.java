package com.yanzu.module.member.dal.mysql.orderinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderInfoMapper extends BaseMapperX<OrderInfoDO> {

    List<OrderInfoDO> getByRoomIds(List<Long> roomIds);

    OrderInfoDO getByUserAndStatus(@Param("userId") Long userId, @Param("status") Integer status);
}
