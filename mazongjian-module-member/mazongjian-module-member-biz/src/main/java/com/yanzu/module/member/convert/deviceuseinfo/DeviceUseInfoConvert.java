package com.yanzu.module.member.convert.deviceuseinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;

/**
 * 设备使用记录 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUseInfoConvert {

    DeviceUseInfoConvert INSTANCE = Mappers.getMapper(DeviceUseInfoConvert.class);

    DeviceUseInfoDO convert(DeviceUseInfoCreateReqVO bean);

    DeviceUseInfoDO convert(DeviceUseInfoUpdateReqVO bean);

    DeviceUseInfoRespVO convert(DeviceUseInfoDO bean);

    List<DeviceUseInfoRespVO> convertList(List<DeviceUseInfoDO> list);

    PageResult<DeviceUseInfoRespVO> convertPage(PageResult<DeviceUseInfoDO> page);

    List<DeviceUseInfoExcelVO> convertList02(List<DeviceUseInfoDO> list);

}
