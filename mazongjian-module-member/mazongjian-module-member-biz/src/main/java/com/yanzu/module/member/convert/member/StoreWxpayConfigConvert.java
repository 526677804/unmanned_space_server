package com.yanzu.module.member.convert.member;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.wxpay.vo.*;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;

/**
 * 门店微信支付配置 Convert
 *
 * @author MrGuan
 */
@Mapper
public interface StoreWxpayConfigConvert {

    StoreWxpayConfigConvert INSTANCE = Mappers.getMapper(StoreWxpayConfigConvert.class);

    StoreWxpayConfigDO convert(StoreWxpayConfigCreateReqVO bean);

    StoreWxpayConfigDO convert(StoreWxpayConfigUpdateReqVO bean);

    StoreWxpayConfigRespVO convert(StoreWxpayConfigDO bean);

    List<StoreWxpayConfigRespVO> convertList(List<StoreWxpayConfigDO> list);

    PageResult<StoreWxpayConfigRespVO> convertPage(PageResult<StoreWxpayConfigDO> page);

    List<StoreWxpayConfigExcelVO> convertList02(List<StoreWxpayConfigDO> list);

}
