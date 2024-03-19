package com.yanzu.module.member.service.deviceuseinfo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoExportReqVO;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoPageReqVO;
import com.yanzu.module.member.controller.admin.deviceuseinfo.vo.DeviceUseInfoRespVO;
import com.yanzu.module.member.dal.dataobject.deviceuseinfo.DeviceUseInfoDO;
import com.yanzu.module.member.dal.mysql.deviceuseinfo.DeviceUseInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 设备使用记录 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class DeviceUseInfoServiceImpl implements DeviceUseInfoService {

    @Resource
    private DeviceUseInfoMapper deviceUseInfoMapper;


    @Override
    public DeviceUseInfoDO getDeviceUseInfo(Long id) {
        return deviceUseInfoMapper.selectById(id);
    }

    @Override
    public List<DeviceUseInfoDO> getDeviceUseInfoList(Collection<Long> ids) {
        return deviceUseInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DeviceUseInfoRespVO> getDeviceUseInfoPage(DeviceUseInfoPageReqVO pageReqVO) {
        IPage<DeviceUseInfoRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        deviceUseInfoMapper.getDeviceUseInfoPage(page,pageReqVO);
        return new PageResult<>(page.getRecords(),page.getTotal());
    }

    @Override
    public List<DeviceUseInfoDO> getDeviceUseInfoList(DeviceUseInfoExportReqVO exportReqVO) {
        return deviceUseInfoMapper.selectList(exportReqVO);
    }

}
