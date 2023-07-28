package com.yanzu.module.member.dal.mysql.deviceinfo;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapperX<DeviceInfoDO> {


    String getDateSnByStoreId(Long storeId);

}
