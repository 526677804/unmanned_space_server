package com.yanzu.module.member.service.facerecord;

import java.util.*;
import javax.validation.*;

import com.yanzu.module.member.controller.admin.faceblacklist.vo.FaceBlacklistAddReqVO;
import com.yanzu.module.member.controller.admin.facerecord.vo.*;
import com.yanzu.module.member.dal.dataobject.facerecord.FaceRecordDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 人脸识别记录 Service 接口
 *
 * @author 超级管理员
 */
public interface FaceRecordService {


    /**
     * 删除人脸识别记录
     *
     * @param id 编号
     */
    void deleteFaceRecord(Long id);

    /**
     * 获得人脸识别记录
     *
     * @param id 编号
     * @return 人脸识别记录
     */
    FaceRecordDO getFaceRecord(Long id);

    /**
     * 获得人脸识别记录列表
     *
     * @param ids 编号
     * @return 人脸识别记录列表
     */
    List<FaceRecordDO> getFaceRecordList(Collection<Long> ids);

    /**
     * 获得人脸识别记录分页
     *
     * @param pageReqVO 分页查询
     * @return 人脸识别记录分页
     */
    PageResult<FaceRecordRespVO> getFaceRecordPage(FaceRecordPageReqVO pageReqVO,boolean isAdmin);

    /**
     * 获得人脸识别记录列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 人脸识别记录列表
     */
    List<FaceRecordDO> getFaceRecordList(FaceRecordExportReqVO exportReqVO);

    void addBlacklist(FaceBlacklistAddReqVO reqVO);

    void moveFaceByRecord(Long id,String remark,boolean isAdmin);

}
