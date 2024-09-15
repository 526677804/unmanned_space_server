package com.yanzu.module.member.service.faceblacklist;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.module.member.controller.admin.facerecord.vo.FaceRecordRespVO;
import com.yanzu.module.member.service.device.DeviceService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.*;
import com.yanzu.module.member.dal.dataobject.faceblacklist.FaceBlacklistDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.faceblacklist.FaceBlacklistConvert;
import com.yanzu.module.member.dal.mysql.faceblacklist.FaceBlacklistMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 人脸黑名单 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class FaceBlacklistServiceImpl implements FaceBlacklistService {

    @Resource
    private FaceBlacklistMapper faceBlacklistMapper;

    @Resource
    private DeviceService deviceService;

    @Override
    @Transactional
    public void deleteFaceBlacklist(Long id) {
        FaceBlacklistDO faceBlacklistDO = faceBlacklistMapper.selectById(id);
        if(!ObjectUtils.isEmpty(faceBlacklistDO)){
            // 先远程删除
            deviceService.delUserFace(faceBlacklistDO.getStoreId(),faceBlacklistDO.getAdmitGuid());
            // 删除数据库
            faceBlacklistMapper.deleteById(id);
        }
    }



    @Override
    public FaceBlacklistDO getFaceBlacklist(Long id) {
        return faceBlacklistMapper.selectById(id);
    }

    @Override
    public List<FaceBlacklistDO> getFaceBlacklistList(Collection<Long> ids) {
        return faceBlacklistMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FaceBlacklistRespVO> getFaceBlacklistPage(FaceBlacklistPageReqVO reqVO) {
        IPage<FaceBlacklistRespVO> page=new Page<>(reqVO.getPageNo(),reqVO.getPageNo());
        faceBlacklistMapper.getFaceBlacklistPage(page,reqVO);
        return new PageResult<>(page.getRecords(),page.getTotal());
    }

    @Override
    public List<FaceBlacklistDO> getFaceBlacklistList(FaceBlacklistExportReqVO exportReqVO) {
        return faceBlacklistMapper.selectList(exportReqVO);
    }

}
