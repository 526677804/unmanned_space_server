package com.yanzu.module.member.dal.mysql.member;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.member.vo.StoreWxpayConfigExportReqVO;
import com.yanzu.module.member.controller.admin.member.vo.StoreWxpayConfigPageReqVO;
import com.yanzu.module.member.controller.admin.member.vo.StoreWxpayConfigPageRespVO;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 门店微信支付配置 Mapper
 *
 * @author MrGuan
 */
@Mapper
public interface StoreWxpayConfigMapper extends BaseMapperX<StoreWxpayConfigDO> {

    default PageResult<StoreWxpayConfigDO> selectPage(StoreWxpayConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StoreWxpayConfigDO>()
                .eqIfPresent(StoreWxpayConfigDO::getStoreId, reqVO.getStoreId())
                .likeIfPresent(StoreWxpayConfigDO::getAppId, reqVO.getAppId())
                .likeIfPresent(StoreWxpayConfigDO::getMchId, reqVO.getMchId())
                .likeIfPresent(StoreWxpayConfigDO::getMchKey, reqVO.getMchKey())
                .orderByDesc(StoreWxpayConfigDO::getId));
    }

    default List<StoreWxpayConfigDO> selectList(StoreWxpayConfigExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreWxpayConfigDO>()
                .eqIfPresent(StoreWxpayConfigDO::getStoreId, reqVO.getStoreId())
                .likeIfPresent(StoreWxpayConfigDO::getAppId, reqVO.getAppId())
                .likeIfPresent(StoreWxpayConfigDO::getMchId, reqVO.getMchId())
                .likeIfPresent(StoreWxpayConfigDO::getMchKey, reqVO.getMchKey())
                .orderByDesc(StoreWxpayConfigDO::getId));
    }

    List<StoreWxpayConfigPageRespVO> getStoreWxpayConfigPage(StoreWxpayConfigPageReqVO reqVO);

    StoreWxpayConfigDO getConfigByStoreId(Long storeId);
}
