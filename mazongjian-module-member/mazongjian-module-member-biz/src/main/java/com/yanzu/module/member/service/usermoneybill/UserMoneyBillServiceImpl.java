package com.yanzu.module.member.service.usermoneybill;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.usermoneybill.vo.*;
import com.yanzu.module.member.dal.dataobject.usermoneybill.UserMoneyBillDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.usermoneybill.UserMoneyBillConvert;
import com.yanzu.module.member.dal.mysql.usermoneybill.UserMoneyBillMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 用户账单明细 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class UserMoneyBillServiceImpl implements UserMoneyBillService {

    @Resource
    private UserMoneyBillMapper userMoneyBillMapper;


    @Override
    public UserMoneyBillDO getUserMoneyBill(Long id) {
        return userMoneyBillMapper.selectById(id);
    }


    @Override
    public PageResult<UserMoneyBillDO> getUserMoneyBillPage(UserMoneyBillPageReqVO pageReqVO) {
        return userMoneyBillMapper.selectPage(pageReqVO);
    }

    @Override
    public List<UserMoneyBillDO> getUserMoneyBillList(UserMoneyBillExportReqVO exportReqVO) {
        return userMoneyBillMapper.selectList(exportReqVO);
    }

}
