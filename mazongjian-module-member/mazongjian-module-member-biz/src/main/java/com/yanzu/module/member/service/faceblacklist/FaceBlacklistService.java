package com.yanzu.module.member.service.faceblacklist;

import java.util.*;
import javax.validation.*;
import com.yanzu.module.member.controller.admin.faceblacklist.vo.*;
import com.yanzu.module.member.dal.dataobject.faceblacklist.FaceBlacklistDO;
import com.yanzu.framework.common.pojo.PageResult;

/**
 * 人脸黑名单 Service 接口
 *
 * @author 超级管理员
 */
public interface FaceBlacklistService {


    /**
     * 删除人脸黑名单
     *
     * @param id 编号
     */
    void deleteFaceBlacklist(Long id);


    /**
     * 获得人脸黑名单分页
     *
     * @param pageReqVO 分页查询
     * @return 人脸黑名单分页
     */
    PageResult<FaceBlacklistRespVO> getFaceBlacklistPage(FaceBlacklistPageReqVO pageReqVO,boolean isAdmin);

    void moveFaceById(Long id);

}
