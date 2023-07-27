package com.yanzu.module.member.convert.orderinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.orderinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.orderinfo.OrderInfoDO;

/**
 * 订单管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface OrderInfoConvert {

    OrderInfoConvert INSTANCE = Mappers.getMapper(OrderInfoConvert.class);

    OrderInfoDO convert(OrderInfoCreateReqVO bean);

    OrderInfoDO convert(OrderInfoUpdateReqVO bean);

    OrderInfoRespVO convert(OrderInfoDO bean);

    List<OrderInfoRespVO> convertList(List<OrderInfoDO> list);

    PageResult<OrderInfoRespVO> convertPage(PageResult<OrderInfoDO> page);

    List<OrderInfoExcelVO> convertList02(List<OrderInfoDO> list);

}
