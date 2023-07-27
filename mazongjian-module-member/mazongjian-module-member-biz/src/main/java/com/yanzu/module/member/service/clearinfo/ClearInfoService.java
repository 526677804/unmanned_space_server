package com.yanzu.module.member.service.clearinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.clearinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 保洁信息管理 Service 接口
 *
 * @author 芋道源码
 */
public interface ClearInfoService {



    /**
     * 更新保洁信息管理
     *
     * @param updateReqVO 更新信息
     */
    void updateClearInfo(@Valid ClearInfoUpdateReqVO updateReqVO);

    /**
     * 获得保洁信息管理
     *
     * @param id 编号
     * @return 保洁信息管理
     */
    ClearInfoDO getClearInfo(Long id);

    /**
     * 获得保洁信息管理列表
     *
     * @param ids 编号
     * @return 保洁信息管理列表
     */
    List<ClearInfoDO> getClearInfoList(Collection<Long> ids);

    /**
     * 获得保洁信息管理分页
     *
     * @param pageReqVO 分页查询
     * @return 保洁信息管理分页
     */
    PageResult<ClearInfoDO> getClearInfoPage(ClearInfoPageReqVO pageReqVO);

    /**
     * 获得保洁信息管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 保洁信息管理列表
     */
    List<ClearInfoDO> getClearInfoList(ClearInfoExportReqVO exportReqVO);

}
