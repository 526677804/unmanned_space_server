package com.yanzu.module.member.service.usermoneybill;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.usermoneybill.vo.*;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 用户账单明细 Service 接口
 *
 * @author 芋道源码
 */
public interface UserMoneyBillService {


    /**
     * 获得用户账单明细
     *
     * @param id 编号
     * @return 用户账单明细
     */
    UserMoneyBillDO getUserMoneyBill(Long id);



    /**
     * 获得用户账单明细分页
     *
     * @param pageReqVO 分页查询
     * @return 用户账单明细分页
     */
    PageResult<UserMoneyBillDO> getUserMoneyBillPage(UserMoneyBillPageReqVO pageReqVO);

    /**
     * 获得用户账单明细列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 用户账单明细列表
     */
    List<UserMoneyBillDO> getUserMoneyBillList(UserMoneyBillExportReqVO exportReqVO);

}
