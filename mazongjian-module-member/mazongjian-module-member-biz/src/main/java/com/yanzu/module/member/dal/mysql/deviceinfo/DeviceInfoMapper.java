package com.yanzu.module.member.dal.mysql.deviceinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;

/**
 * 设备管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapperX<DeviceInfoDO> {

    default PageResult<DeviceInfoDO> selectPage(DeviceInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceInfoDO>()
                .eqIfPresent(DeviceInfoDO::getDeviceSn, reqVO.getDeviceSn())
                .eqIfPresent(DeviceInfoDO::getType, reqVO.getType())
                .eqIfPresent(DeviceInfoDO::getRoomId, reqVO.getRoomId())
                .eqIfPresent(DeviceInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(DeviceInfoDO::getId)
        );
    }

    default List<DeviceInfoDO> selectList(DeviceInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceInfoDO>()
                .eqIfPresent(DeviceInfoDO::getDeviceSn, reqVO.getDeviceSn())
                .eqIfPresent(DeviceInfoDO::getType, reqVO.getType())
                .eqIfPresent(DeviceInfoDO::getRoomId, reqVO.getRoomId())
                .eqIfPresent(DeviceInfoDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(DeviceInfoDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(DeviceInfoDO::getId)
        );
    }

}
