package com.yanzu.module.member.service.deviceinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 设备管理 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceInfoService {

    /**
     * 创建设备管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    void createDeviceInfo(@Valid DeviceInfoCreateReqVO createReqVO);


    /**
     * 删除设备管理
     *
     * @param id 编号
     */
    void deleteDeviceInfo(Long id);

    /**
     * 获得设备管理
     *
     * @param id 编号
     * @return 设备管理
     */
    DeviceInfoDO getDeviceInfo(Long id);

    /**
     * 获得设备管理列表
     *
     * @param ids 编号
     * @return 设备管理列表
     */
    List<DeviceInfoDO> getDeviceInfoList(Collection<Long> ids);

    /**
     * 获得设备管理分页
     *
     * @param pageReqVO 分页查询
     * @return 设备管理分页
     */
    PageResult<DeviceInfoRespVO> getDeviceInfoPage(DeviceInfoPageReqVO pageReqVO,boolean isAdmin);

    /**
     * 获得设备管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备管理列表
     */
    List<DeviceInfoDO> getDeviceInfoList(DeviceInfoExportReqVO exportReqVO);

    void bind(DeviceInfoBindReqVO reqVO);

    void configWifi(DeviceInfoConfigWifiReqVO reqVO);

    void setLockAutoLock(DeviceInfoSetAutoLockReqVO reqVO);

    void control(DeviceControlReqVO reqVO);

}
