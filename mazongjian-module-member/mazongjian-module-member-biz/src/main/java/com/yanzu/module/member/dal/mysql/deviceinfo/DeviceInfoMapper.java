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
import com.yanzu.module.member.service.iot.device.IotDeviceRoomInfoVO;
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

    default List<DeviceInfoDO> getByRoomIdAndType(Long roomId, Integer[] type) {
        return selectList(new LambdaQueryWrapperX<DeviceInfoDO>()
                .eqIfPresent(DeviceInfoDO::getRoomId, roomId)
                .inIfPresent(DeviceInfoDO::getType, type));
    }

    String getSnByStoreIdAndType(@Param("storeId") Long storeId, @Param("type") Integer type);


    List<DeviceInfoDO> getByRoomId(@Param("roomId") Long roomId);

    IPage<DeviceInfoRespVO> getDeviceInfoPage(@Param("page") IPage<DeviceInfoRespVO> page, @Param("reqVO") DeviceInfoPageReqVO reqVO
            , @Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    int updateStatusBySN(@Param("deviceSn") String deviceSn, @Param("state") Integer state);

    int countGateway(Long storeId);

    @TenantIgnore
    int countBySN(String deviceSn);

    int updateBindInfo(@Param("deviceId") Long deviceId, @Param("roomId") Long roomId);

    IotDeviceRoomInfoVO getDeviceRoomVO(@Param("deviceSn") String deviceSn);

    List<IotDeviceRoomInfoVO> getStoreVoice(Long storeId);

    Long getTenantBySN(String deviceSn);

    int countByTypeAndRoomId(@Param("deviceType") Integer deviceType, @Param("roomId") Long roomId);

    int countKongtiao(Long roomId);

    int deleteStoreId(Long id);

    List<DeviceInfoDO> getIRListByRoomId(Long roomId);

    int countShare(String deviceSn);

}
