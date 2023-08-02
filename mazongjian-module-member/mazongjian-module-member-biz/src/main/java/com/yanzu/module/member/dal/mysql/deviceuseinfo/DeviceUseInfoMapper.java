package com.yanzu.module.member.dal.mysql.deviceuseinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoExportReqVO;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoPageReqVO;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
                .eqIfPresent(DeviceUseInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(DeviceUseInfoDO::getRoomId, reqVO.getRoomId())
                .likeIfPresent(DeviceUseInfoDO::getCmd, reqVO.getCmd())
                .betweenIfPresent(DeviceUseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUseInfoDO::getId));
    }

    default List<DeviceUseInfoDO> selectList(DeviceUseInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceUseInfoDO>()
                .eqIfPresent(DeviceUseInfoDO::getUserId, reqVO.getUserId())
                .eqIfPresent(DeviceUseInfoDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(DeviceUseInfoDO::getRoomId, reqVO.getRoomId())
                .likeIfPresent(DeviceUseInfoDO::getCmd, reqVO.getCmd())
                .betweenIfPresent(DeviceUseInfoDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeviceUseInfoDO::getId));
    }


}
