package com.yanzu.module.member.service.deviceuseinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoExportReqVO;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoPageReqVO;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoRespVO;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;

import java.util.Collection;
import java.util.List;

/**
 * 设备使用记录 Service 接口
 *
 * @author 芋道源码
 */
public interface DeviceUseInfoService {

    /**
     * 获得设备使用记录
     *
     * @param id 编号
     * @return 设备使用记录
     */
    DeviceUseInfoDO getDeviceUseInfo(Long id);

    /**
     * 获得设备使用记录列表
     *
     * @param ids 编号
     * @return 设备使用记录列表
     */
    List<DeviceUseInfoDO> getDeviceUseInfoList(Collection<Long> ids);

    /**
     * 获得设备使用记录分页
     *
     * @param pageReqVO 分页查询
     * @return 设备使用记录分页
     */
    PageResult<DeviceUseInfoRespVO> getDeviceUseInfoPage(DeviceUseInfoPageReqVO pageReqVO);

    /**
     * 获得设备使用记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 设备使用记录列表
     */
    List<DeviceUseInfoDO> getDeviceUseInfoList(DeviceUseInfoExportReqVO exportReqVO);

}
