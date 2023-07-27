package com.yanzu.module.member.service.clearbill;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.clearbill.vo.*;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 保洁账单管理 Service 接口
 *
 * @author 芋道源码
 */
public interface ClearBillService {


    /**
     * 删除保洁账单管理
     *
     * @param id 编号
     */
    void deleteClearBill(Long id);

    /**
     * 获得保洁账单管理
     *
     * @param id 编号
     * @return 保洁账单管理
     */
    ClearBillDO getClearBill(Long id);


    /**
     * 获得保洁账单管理分页
     *
     * @param pageReqVO 分页查询
     * @return 保洁账单管理分页
     */
    PageResult<ClearBillDO> getClearBillPage(ClearBillPageReqVO pageReqVO);

    /**
     * 获得保洁账单管理列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 保洁账单管理列表
     */
    List<ClearBillDO> getClearBillList(ClearBillExportReqVO exportReqVO);

}
