package com.yanzu.module.member.service.faceblacklist;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.module.member.controller.admin.facerecord.vo.FaceRecordRespVO;
import com.yanzu.module.member.dal.mysql.facerecord.FaceRecordMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
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
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
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
    private FaceRecordMapper faceRecordMapper;

    @Resource
    private DeviceService deviceService;

    @Resource
    private StoreInfoService storeInfoService;


    @Override
    @Transactional
    public void deleteFaceBlacklist(Long id) {
        FaceBlacklistDO faceBlacklistDO = faceBlacklistMapper.selectById(id);
        if (!ObjectUtils.isEmpty(faceBlacklistDO)) {
            // 先远程删除
            deviceService.delUserFace(faceBlacklistDO.getStoreId(), faceBlacklistDO.getAdmitGuid());
            // 删除数据库
            faceBlacklistMapper.deleteById(id);
            //删除后把此guid的所有记录 改成STRANGERBABY
            faceRecordMapper.delGuid(faceBlacklistDO.getAdmitGuid());
        }
    }


    @Override
    public PageResult<FaceBlacklistRespVO> getFaceBlacklistPage(FaceBlacklistPageReqVO reqVO, boolean isAdmin) {
        //检查权限
        storeInfoService.checkPermisson(null, null, getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        IPage<FaceBlacklistRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        faceBlacklistMapper.getFaceBlacklistPage(page, reqVO, getLoginUserId(), isAdmin);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }


    @Override
    @Transactional
    public void moveFaceById(Long id) {
        FaceBlacklistDO faceBlacklistDO = faceBlacklistMapper.selectById(id);
        if (!ObjectUtils.isEmpty(faceBlacklistDO)) {
            //检查权限
            storeInfoService.checkPermisson(faceBlacklistDO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
            deviceService.delUserFace(faceBlacklistDO.getStoreId(),faceBlacklistDO.getAdmitGuid());
            faceBlacklistMapper.deleteById(id);
            //删除后把此guid的所有记录 改成STRANGERBABY
            faceRecordMapper.delGuid(faceBlacklistDO.getAdmitGuid());
        }
    }

}
