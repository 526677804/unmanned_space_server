package com.yanzu.module.member.service.bannerinfo;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.bannerinfo.vo.*;
import com.yanzu.module.member.dal.dataobject.bannerinfo.BannerInfoDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.bannerinfo.BannerInfoConvert;
import com.yanzu.module.member.dal.mysql.bannerinfo.BannerInfoMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 广告管理 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class BannerInfoServiceImpl implements BannerInfoService {

    @Resource
    private BannerInfoMapper bannerInfoMapper;

    @Override
    public Long createBannerInfo(BannerInfoCreateReqVO createReqVO) {
        // 插入
        BannerInfoDO bannerInfo = BannerInfoConvert.INSTANCE.convert(createReqVO);
        bannerInfoMapper.insert(bannerInfo);
        // 返回
        return bannerInfo.getId();
    }

    @Override
    public void updateBannerInfo(BannerInfoUpdateReqVO updateReqVO) {
        // 校验存在
        validateBannerInfoExists(updateReqVO.getId());
        // 更新
        BannerInfoDO updateObj = BannerInfoConvert.INSTANCE.convert(updateReqVO);
        bannerInfoMapper.updateById(updateObj);
    }

    @Override
    public void deleteBannerInfo(Long id) {
        // 校验存在
        validateBannerInfoExists(id);
        // 删除
        bannerInfoMapper.deleteById(id);
    }

    private void validateBannerInfoExists(Long id) {
        if (bannerInfoMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public BannerInfoDO getBannerInfo(Long id) {
        return bannerInfoMapper.selectById(id);
    }

    @Override
    public List<BannerInfoDO> getBannerInfoList(Collection<Long> ids) {
        return bannerInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<BannerInfoDO> getBannerInfoPage(BannerInfoPageReqVO pageReqVO) {
        return bannerInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BannerInfoDO> getBannerInfoList(BannerInfoExportReqVO exportReqVO) {
        return bannerInfoMapper.selectList(exportReqVO);
    }

}
