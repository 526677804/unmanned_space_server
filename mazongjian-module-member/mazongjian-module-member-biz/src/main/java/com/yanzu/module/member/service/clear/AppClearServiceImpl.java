package com.yanzu.module.member.service.clear;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.clear.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.mysql.clearbill.ClearBillMapper;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

@Service
@Validated
public class AppClearServiceImpl implements AppClearService {

    @Resource
    private ClearInfoMapper clearInfoMapper;

    @Resource
    private ClearBillMapper clearBillMapper;

    @Resource
    private OrderInfoMapper orderInfoMapper;


    @Override
    public PageResult<AppClearPageRespVO> getClearPage(AppClearPageReqVO reqVO) {
        if (!ObjectUtils.isEmpty(reqVO.getStatus())) {
            //如果是查询待接单的，那就不用过滤userId
            if (reqVO.getStatus().intValue() != 0) {
                reqVO.setUserId(getLoginUserId());
            }
        }
        PageHelper.startPage(reqVO);
        List<AppClearPageRespVO> list = orderInfoMapper.getClearPage(reqVO);
        PageInfo<AppClearPageRespVO> page = new PageInfo<>(list);
        return new PageResult<>(page.getList(), page.getTotal());
    }

    @Override
    @Transactional
    public void changeStatus(Long id, Integer status) {
        ClearInfoDO clearInfoDO = clearInfoMapper.selectById(id);
        //接单1/开始2/取消3
        switch (status) {
            case 1:
                //没有被别人接单，才能接单
                if (ObjectUtils.isEmpty(clearInfoDO.getUserId())) {
                    clearInfoDO.setUserId(getLoginUserId());
                    clearInfoDO.setCreateTime(LocalDateTime.now());
                    clearInfoMapper.updateById(clearInfoDO);
                } else {
                    throw exception(CLEAR_ORDER_NOT_JIEDAN);
                }
                break;
            default:
                //只有自己的单子 才能改状态
                if (clearInfoDO.getUserId().compareTo(getLoginUserId()) == 0) {
                    switch (status) {
                        case 2:
                            //开始订单  只有已接单状态才能开始
                            clearInfoDO.setStatus(AppEnum.clear_info_status.START.getValue());
                            if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.JIEDAN.getValue()) != 0) {
                                throw exception(CLEAR_ORDER_STATUS_ERROR);
                            }
                            break;
                        case 3:
                            //取消订单 只有已接单状态才能取消
                            clearInfoDO.setStatus(AppEnum.clear_info_status.CANCEL.getValue());
                            clearInfoDO.setUserId(null);
                            if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.JIEDAN.getValue()) != 0) {
                                throw exception(CLEAR_ORDER_STATUS_ERROR);
                            }
                            break;
                    }
                    clearInfoMapper.updateById(clearInfoDO);
                } else {
                    throw exception(OPRATION_ERROR);
                }
                break;
        }
    }

    @Override
    public void openStoreDoor(Long id) {

    }

    @Override
    public void openRoomDoor(Long id) {

    }

    @Override
    public AppClearInfoRespVO getDetail(Long id) {
        return null;
    }

    @Override
    public AppClearChartRespVO getChartData() {
        return null;
    }

    @Override
    public PageResult<AppClearBillRespVO> getClearBillPage(AppClearBillReqVO reqVO) {
        return null;
    }

    @Override
    @Transactional
    public void finish(AppStartClearReqVO reqVO) {

    }
}
