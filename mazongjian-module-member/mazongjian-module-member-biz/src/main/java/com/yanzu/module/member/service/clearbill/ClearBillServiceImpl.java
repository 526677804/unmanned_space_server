package com.yanzu.module.member.service.clearbill;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.clearbill.vo.*;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.clearbill.ClearBillConvert;
import com.yanzu.module.member.dal.mysql.clearbill.ClearBillMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 保洁账单管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ClearBillServiceImpl implements ClearBillService {

    @Resource
    private ClearBillMapper clearBillMapper;

    @Override
    public void deleteClearBill(Long id) {
        // 删除
        clearBillMapper.deleteById(id);
    }


    @Override
    public ClearBillDO getClearBill(Long id) {
        return clearBillMapper.selectById(id);
    }

    @Override
    public PageResult<ClearBillDO> getClearBillPage(ClearBillPageReqVO pageReqVO) {
        return clearBillMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ClearBillDO> getClearBillList(ClearBillExportReqVO exportReqVO) {
        return clearBillMapper.selectList(exportReqVO);
    }

}
