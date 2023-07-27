package com.yanzu.module.member.service.franchiseinfo;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 加盟信息 Service 接口
 *
 * @author 芋道源码
 */
public interface FranchiseInfoService {

    /**
     * 创建加盟信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFranchiseInfo(@Valid FranchiseInfoCreateReqVO createReqVO);



    /**
     * 删除加盟信息
     *
     * @param id 编号
     */
    void deleteFranchiseInfo(Long id);

    /**
     * 获得加盟信息
     *
     * @param id 编号
     * @return 加盟信息
     */
    FranchiseInfoDO getFranchiseInfo(Long id);

    /**
     * 获得加盟信息列表
     *
     * @param ids 编号
     * @return 加盟信息列表
     */
    List<FranchiseInfoDO> getFranchiseInfoList(Collection<Long> ids);

    /**
     * 获得加盟信息分页
     *
     * @param pageReqVO 分页查询
     * @return 加盟信息分页
     */
    PageResult<FranchiseInfoDO> getFranchiseInfoPage(FranchiseInfoPageReqVO pageReqVO);

    /**
     * 获得加盟信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 加盟信息列表
     */
    List<FranchiseInfoDO> getFranchiseInfoList(FranchiseInfoExportReqVO exportReqVO);

}
