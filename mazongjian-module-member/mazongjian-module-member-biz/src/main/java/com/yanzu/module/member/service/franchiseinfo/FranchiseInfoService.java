package com.yanzu.module.member.service.franchiseinfo;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoExportReqVO;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoPageReqVO;
import com.yanzu.module.member.controller.admin.franchiseinfo.vo.FranchiseInfoUpdateReqVO;
import com.yanzu.module.member.dal.dataobject.franchiseinfo.FranchiseInfoDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

/**
 * 加盟信息 Service 接口
 *
 * @author yanzu
 */
public interface FranchiseInfoService {
    /**
     * 更新加盟信息
     *
     * @param updateReqVO 更新信息
     */
    void updateFranchiseInfo(@Valid FranchiseInfoUpdateReqVO updateReqVO);

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
