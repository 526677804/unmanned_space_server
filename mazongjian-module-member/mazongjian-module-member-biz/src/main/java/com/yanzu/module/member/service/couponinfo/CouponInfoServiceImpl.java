package com.yanzu.module.member.service.couponinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.couponinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.couponinfo.CouponInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.couponinfo.CouponInfoConvert;
import com.yanzu.module.member.dal.mysql.couponinfo.CouponInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 优惠券管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CouponInfoServiceImpl implements CouponInfoService {

    @Resource
    private CouponInfoMapper couponInfoMapper;

    @Override
    public Long createCouponInfo(CouponInfoCreateReqVO createReqVO) {
        // 插入
        CouponInfoDO couponInfo = CouponInfoConvert.INSTANCE.convert(createReqVO);
        couponInfoMapper.insert(couponInfo);
        // 返回
        return couponInfo.getCouponId();
    }

    @Override
    public void updateCouponInfo(CouponInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateCouponInfoExists(updateReqVO.getCouponId());
        // 更新
        CouponInfoDO updateObj = CouponInfoConvert.INSTANCE.convert(updateReqVO);
        couponInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteCouponInfo(Long id) {
        // 校验存在
        validateCouponInfoExists(id);
        // 删除
        couponInfoMapper.deleteById(id);
    }

    private void validateCouponInfoExists(Long id) {
        if (couponInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public CouponInfoDO getCouponInfo(Long id) {
        return couponInfoMapper.selectById(id);
    }

    @Override
    public List<CouponInfoDO> getCouponInfoList(Collection<Long> ids) {
        return couponInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<CouponInfoDO> getCouponInfoPage(CouponInfoPageReqVO pageReqVO) {
        return couponInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CouponInfoDO> getCouponInfoList(CouponInfoExportReqVO exportReqVO) {
        return couponInfoMapper.selectList(exportReqVO);
    }

}
