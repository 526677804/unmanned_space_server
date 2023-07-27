package com.yanzu.module.member.dal.mysql.deviceuseinfo;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import org.apache.ibatis.annotations.Mapper;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.*;

/**
 * 设备使用记录 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceUseInfoMapper extends BaseMapperX<DeviceUseInfoDO> {

    default PageResult<DeviceUseInfoDO> selectPage(DeviceUseInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceUseInfoDO>()
                .eqIfPresent(DeviceUseInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUseInfoDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceUseInfoDO::getDeviceNo, reqVO.getDeviceNo())
                .likeIfPresent(DeviceUseInfoDO::getCmd, reqVO.getCmd())
                .betweenIfPresent(DeviceUseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUseInfoDO::getId));
    }

    default List<DeviceUseInfoDO> selectList(DeviceUseInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceUseInfoDO>()
                .eqIfPresent(DeviceUseInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUseInfoDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(DeviceUseInfoDO::getDeviceNo, reqVO.getDeviceNo())
                .likeIfPresent(DeviceUseInfoDO::getCmd, reqVO.getCmd())
                .betweenIfPresent(DeviceUseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUseInfoDO::getId));
    }

}
