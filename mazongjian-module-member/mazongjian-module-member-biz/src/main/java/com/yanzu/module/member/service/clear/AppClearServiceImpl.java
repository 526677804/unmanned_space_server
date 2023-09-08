package com.yanzu.module.member.service.clear;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.common.util.date.DateUtils;
import com.yanzu.module.member.controller.app.clear.vo.*;
import com.yanzu.module.member.dal.dataobject.clearinfo.ClearInfoDO;
import com.yanzu.module.member.dal.mysql.clearbill.ClearBillMapper;
import com.yanzu.module.member.dal.mysql.clearinfo.ClearInfoMapper;
import com.yanzu.module.member.dal.mysql.orderinfo.OrderInfoMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
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

    @Resource
    private DeviceService deviceService;

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
                    clearInfoDO.setStatus(status);
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
                            if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.JIEDAN.getValue()) != 0) {
                                throw exception(CLEAR_ORDER_STATUS_ERROR);
                            }
                            clearInfoDO.setStatus(AppEnum.clear_info_status.START.getValue());
                            clearInfoDO.setStartTime(LocalDateTime.now());
                            break;
                        case 3:
                            //取消订单 只有已接单状态才能取消
                            clearInfoDO.setUserId(null);
                            if (clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.JIEDAN.getValue()) != 0) {
                                throw exception(CLEAR_ORDER_STATUS_ERROR);
                            }
                            clearInfoDO.setStatus(AppEnum.clear_info_status.CANCEL.getValue());
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
        //获取任务信息
        ClearInfoDO clearInfoDO = clearInfoMapper.selectById(id);
        //只有是自己的订单 才能开门
        if (clearInfoDO.getUserId().compareTo(getLoginUserId()) == 0) {
            deviceService.openRoomDoor(clearInfoDO.getRoomId(), null, 3);
        } else {
            throw exception(CLEAR_OPEN_DOOR_ERROR);
        }
    }

    @Override
    public void openRoomDoor(Long id) {
        //获取任务信息
        ClearInfoDO clearInfoDO = clearInfoMapper.selectById(id);
        //只有是自己的订单 并且状态是已开始 才能开门
        if (clearInfoDO.getUserId().compareTo(getLoginUserId()) == 0 && clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.START.getValue()) == 0) {
            deviceService.openRoomDoor(clearInfoDO.getRoomId(), null, 3);
        } else {
            throw exception(CLEAR_OPEN_DOOR_ERROR);
        }
    }

    @Override
    public AppClearInfoRespVO getDetail(Long clearId) {
        return clearInfoMapper.getDetail(clearId);
    }

    @Override
    public AppClearChartRespVO getChartData() {
        AppClearChartRespVO respVO = new AppClearChartRespVO();
        //获取今日的时间
        String today = DateUtils.dateToStr(new Date(), DateUtils.FORMAT_YEAR_MONTH_DAY);
        Long loginUserId = getLoginUserId();
        //1=日 2=月 3=全部
        respVO.setTodayJiedan(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.JIEDAN.getValue(), 1, today));
        respVO.setTodayStart(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.START.getValue(), 1, today));
        respVO.setTodayFinish(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.FINISH.getValue(), 1, today));
        respVO.setTomonthJiesuan(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.JIESUAN.getValue(), 2, today));
        respVO.setTomonthFinish(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.FINISH.getValue(), 2, today));
        respVO.setTomonthBohui(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.TOUSU.getValue(), 2, today));
        respVO.setTotalFinish(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.FINISH.getValue(), 3, null));
        respVO.setTotalSettlementt(clearInfoMapper.countByUserIdAndStatus(loginUserId, AppEnum.clear_info_status.JIESUAN.getValue(), 3, null));
        //总收入
        respVO.setTotalMoney(clearBillMapper.sumMoneyByUserId(loginUserId));
        return respVO;
    }

    @Override
    public PageResult<AppClearBillRespVO> getClearBillPage(AppClearBillReqVO reqVO) {
        reqVO.setUserId(getLoginUserId());
        PageHelper.startPage(reqVO);
        List<AppClearBillRespVO> list = clearBillMapper.getClearBillPage(reqVO);
        PageInfo<AppClearBillRespVO> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    @Transactional
    public void finish(AppStartClearReqVO reqVO) {
        ClearInfoDO clearInfoDO = clearInfoMapper.selectById(reqVO.getId());
        //只有自己的单子 并且状态是已开始 才能改状态
        if (clearInfoDO.getUserId().compareTo(getLoginUserId()) == 0
                && clearInfoDO.getStatus().compareTo(AppEnum.clear_info_status.START.getValue()) == 0) {
            //校验图片的数量
            if (StringUtils.isEmpty(clearInfoDO.getImgs())) {
                throw exception(CLEAR_IMAGE_NOT_FOUNT_ERROR);
            }
            clearInfoDO.setStatus(AppEnum.clear_info_status.FINISH.getValue());
            clearInfoDO.setFinishTime(LocalDateTime.now());
            clearInfoMapper.updateById(clearInfoDO);
        } else {
            throw exception(OPRATION_ERROR);
        }
    }
}
