package com.yanzu.module.member.dal.mysql.deviceinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.tenant.core.aop.TenantIgnore;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoExportReqVO;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoPageReqVO;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.DeviceInfoRespVO;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface DeviceInfoMapper extends BaseMapperX<DeviceInfoDO> {

    default PageResult<DeviceInfoDO> selectPage(DeviceInfoPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeviceInfoDO>().likeIfPresent(DeviceInfoDO::getDeviceSn, reqVO.getDeviceSn()).eqIfPresent(DeviceInfoDO::getType, reqVO.getType()).eqIfPresent(DeviceInfoDO::getRoomId, reqVO.getRoomId()).eqIfPresent(DeviceInfoDO::getStoreId, reqVO.getStoreId()).eqIfPresent(DeviceInfoDO::getStatus, reqVO.getStatus()).betweenIfPresent(DeviceInfoDO::getCreateTime, reqVO.getCreateTime()).orderByDesc(DeviceInfoDO::getDeviceId));
    }

    default List<DeviceInfoDO> selectList(DeviceInfoExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DeviceInfoDO>().likeIfPresent(DeviceInfoDO::getDeviceSn, reqVO.getDeviceSn()).eqIfPresent(DeviceInfoDO::getType, reqVO.getType()).eqIfPresent(DeviceInfoDO::getRoomId, reqVO.getRoomId()).eqIfPresent(DeviceInfoDO::getStoreId, reqVO.getStoreId()).eqIfPresent(DeviceInfoDO::getStatus, reqVO.getStatus()).betweenIfPresent(DeviceInfoDO::getCreateTime, reqVO.getCreateTime()).orderByDesc(DeviceInfoDO::getDeviceId));
    }

    String getSnByStoreIdAndType(@Param("storeId")Long storeId,@Param("type") Integer type);

    String getSnByRoomIdAndType(@Param("roomId") Long roomId, @Param("type") Integer type);

    List<DeviceInfoDO> getByRoomId(@Param("roomId") Long roomId);

    IPage<DeviceInfoRespVO> getDeviceInfoPage(@Param("page") IPage<DeviceInfoRespVO> page, @Param("reqVO") DeviceInfoPageReqVO reqVO
            , @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    int updateStatusBySN(@Param("deviceSn") String deviceSn, @Param("state") Integer state);

    int countGateway(Long storeId);

    @TenantIgnore
    int countBySN(String deviceSn);

    int updateBindInfo(@Param("deviceId") Long deviceId, @Param("storeId") Long storeId, @Param("roomId") Long roomId);
}
