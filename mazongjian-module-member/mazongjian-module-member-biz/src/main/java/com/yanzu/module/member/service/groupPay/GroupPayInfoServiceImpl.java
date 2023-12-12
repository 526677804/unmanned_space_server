package com.yanzu.module.member.service.groupPay;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoExportReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoPageReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoRespVO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.dal.mysql.groupPay.GroupPayInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 团购支付信息 Service 实现类
 *
 * @author MrGuan
 */
@Service
@Validated
public class GroupPayInfoServiceImpl implements GroupPayInfoService {

    @Resource
    private GroupPayInfoMapper groupPayInfoMapper;

    @Override
    public GroupPayInfoDO getGroupPayInfo(Long id) {
        return groupPayInfoMapper.selectById(id);
    }

    @Override
    public List<GroupPayInfoDO> getGroupPayInfoList(Collection<Long> ids) {
        return groupPayInfoMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<GroupPayInfoRespVO> getGroupPayInfoPage(GroupPayInfoPageReqVO pageReqVO) {
        PageHelper.startPage(pageReqVO);
        List<GroupPayInfoRespVO> list = groupPayInfoMapper.getPage(pageReqVO);
        PageInfo<GroupPayInfoRespVO> pageInfo = new PageInfo<>(list);
        return new PageResult<>(pageInfo.getList(), pageInfo.getTotal());
    }

    @Override
    public List<GroupPayInfoDO> getGroupPayInfoList(GroupPayInfoExportReqVO exportReqVO) {
        return groupPayInfoMapper.selectList(exportReqVO);
    }

}
