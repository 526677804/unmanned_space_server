package com.yanzu.module.member.service.deviceinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.deviceinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.deviceinfo.DeviceInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

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
    Long createDeviceInfo(@Valid DeviceInfoCreateReqVO createReqVO);

    /**
     * 更新设备管理
     *
     * @param updateReqVO 更新信息
     */
    void updateDeviceInfo(@Valid DeviceInfoUpdateReqVO updateReqVO);

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
    PageResult<DeviceInfoDO> getDeviceInfoPage(DeviceInfoPageReqVO pageReqVO);

    /**
     * 获得设备管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备管理列表
     */
    List<DeviceInfoDO> getDeviceInfoList(DeviceInfoExportReqVO exportReqVO);

}
