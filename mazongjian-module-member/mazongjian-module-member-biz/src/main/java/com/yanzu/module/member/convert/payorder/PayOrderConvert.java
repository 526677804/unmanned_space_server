package com.yanzu.module.member.convert.payorder;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.payorder.vo.*;
import com.yanzu.module.member.dal.dataobject.payorder.PayOrderDO;

/**
 * 支付订单 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrderDO convert(PayOrderCreateReqVO bean);

    PayOrderDO convert(PayOrderUpdateReqVO bean);

    PayOrderRespVO convert(PayOrderDO bean);

    List<PayOrderRespVO> convertList(List<PayOrderDO> list);

    PageResult<PayOrderRespVO> convertPage(PageResult<PayOrderDO> page);

    List<PayOrderExcelVO> convertList02(List<PayOrderDO> list);

}
