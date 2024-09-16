package com.yanzu.module.member.service.facerecord;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.FaceBlacklistAddReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppFaceRecordRespVO;
import com.yanzu.module.member.dal.dataobject.faceblacklist.FaceBlacklistDO;
import com.yanzu.module.member.dal.mysql.faceblacklist.FaceBlacklistMapper;
import com.yanzu.module.member.enums.AppEnum;
import com.yanzu.module.member.service.device.DeviceService;
import com.yanzu.module.member.service.storeinfo.StoreInfoService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yanzu.module.member.controller.admin.facerecord.vo.*;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.framework.common.pojo.PageResult;

import com.yanzu.module.member.convert.facerecord.FaceRecordConvert;
import com.yanzu.module.member.dal.mysql.facerecord.FaceRecordMapper;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.yanzu.framework.web.core.util.WebFrameworkUtils.getLoginUserType;
import static com.yanzu.module.member.enums.ErrorCodeConstants.*;

/**
 * 人脸识别记录 Service 实现类
 *
 * @author 超级管理员
 */
@Service
@Validated
public class FaceRecordServiceImpl implements FaceRecordService {

    @Resource
    private FaceRecordMapper faceRecordMapper;

    @Resource
    private DeviceService deviceService;

    @Resource
    private StoreInfoService storeInfoService;

    @Resource
    private FaceBlacklistMapper faceBlacklistMapper;

    @Override
    public void deleteFaceRecord(Long id) {
        // 校验存在
        validateFaceRecordExists(id);
        // 删除
        faceRecordMapper.deleteById(id);
    }

    private void validateFaceRecordExists(Long id) {
        if (faceRecordMapper.selectById(id) == null) {
            throw exception(DATA_NOT_EXISTS);
        }
    }

    @Override
    public FaceRecordDO getFaceRecord(Long id) {
        return faceRecordMapper.selectById(id);
    }

    @Override
    public List<FaceRecordDO> getFaceRecordList(Collection<Long> ids) {
        return faceRecordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FaceRecordRespVO> getFaceRecordPage(FaceRecordPageReqVO reqVO,boolean isAdmin) {
        //检查权限
        storeInfoService.checkPermisson(reqVO.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
        IPage<FaceRecordRespVO> page=new Page<>(reqVO.getPageNo(),reqVO.getPageSize());
        faceRecordMapper.getFaceRecordPage(page,reqVO, getLoginUserId(),isAdmin);
        return new PageResult<>(page.getRecords(),page.getTotal());
    }

    @Override
    public List<FaceRecordDO> getFaceRecordList(FaceRecordExportReqVO exportReqVO) {
        return faceRecordMapper.selectList(exportReqVO);
    }

    @Override
    @Transactional
    public void addBlacklist(FaceBlacklistAddReqVO reqVO) {
        FaceRecordRespVO recordRespVO=faceRecordMapper.getById(reqVO.getId());
        if(!ObjectUtils.isEmpty(recordRespVO)){
            //先进行人脸注册
            String guid = deviceService.addUserFace(recordRespVO.getStoreId(), recordRespVO.getPhotoUrl(), reqVO.getRemark());
            //然后加到数据库
            FaceBlacklistDO faceBlacklistDO=new FaceBlacklistDO()
                    .setRemark(reqVO.getRemark())
                    .setStoreId(recordRespVO.getStoreId())
                    .setPhotoUrl(recordRespVO.getPhotoUrl())
                    .setAdmitGuid(guid)
                    .setUserId(getLoginUserId());
            faceBlacklistMapper.insert(faceBlacklistDO);
        }
    }

    @Override
    @Transactional
    public void moveFaceByRecord(Long id,String remark) {
        FaceRecordRespVO vo = faceRecordMapper.getById(id);
        if(!ObjectUtils.isEmpty(vo)){
            //检查权限
            storeInfoService.checkPermisson(vo.getStoreId(), getLoginUserId(), getLoginUserType(), AppEnum.member_user_type.ADMIN.getValue());
            //判断该用户是否已存在黑名单
            FaceBlacklistDO blacklistDO = faceBlacklistMapper.getByStoreAndGuid(vo.getStoreId(), vo.getAdmitGuid());
            if(ObjectUtils.isEmpty(blacklistDO)){
                //添加
                String guid = deviceService.addUserFace(vo.getStoreId(), vo.getPhotoUrl(), remark);
                blacklistDO=new FaceBlacklistDO()
                        .setUserId(getLoginUserId())
                        .setStoreId(vo.getStoreId())
                        .setPhotoUrl(vo.getPhotoUrl())
                        .setAdmitGuid(guid)
                        .setRemark(remark);
                faceBlacklistMapper.insert(blacklistDO);
            }else{
                //移出
                deviceService.delUserFace(vo.getStoreId(), vo.getAdmitGuid());
                faceBlacklistMapper.deleteById(blacklistDO.getBlacklistId());
            }
        }
    }

}
