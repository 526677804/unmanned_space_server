package com.yanzu.module.member.dal.mysql.member;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.wxpay.vo.StoreWxpayConfigExportReqVO;
import com.yanzu.module.member.controller.admin.wxpay.vo.StoreWxpayConfigPageReqVO;
import com.yanzu.module.member.controller.admin.wxpay.vo.StoreWxpayConfigPageRespVO;
import com.yanzu.module.member.dal.dataobject.member.StoreWxpayConfigDO;
import com.yanzu.module.member.service.wx.MiniappConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
                .eqIfPresent(StoreWxpayConfigDO::getSplit, reqVO.getSplit())
                .eqIfPresent(StoreWxpayConfigDO::getServiceModel, reqVO.getServiceModel())
                .likeIfPresent(StoreWxpayConfigDO::getMchId, reqVO.getMchId())
                .orderByDesc(StoreWxpayConfigDO::getId));
    }

    default List<StoreWxpayConfigDO> selectList(StoreWxpayConfigExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreWxpayConfigDO>()
                .eqIfPresent(StoreWxpayConfigDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(StoreWxpayConfigDO::getSplit, reqVO.getSplit())
                .eqIfPresent(StoreWxpayConfigDO::getServiceModel, reqVO.getServiceModel())
                .likeIfPresent(StoreWxpayConfigDO::getMchId, reqVO.getMchId())
                .orderByDesc(StoreWxpayConfigDO::getId));
    }

    IPage<StoreWxpayConfigPageRespVO> getStoreWxpayConfigPage(@Param("page") IPage<StoreWxpayConfigPageRespVO> page, @Param("reqVO") StoreWxpayConfigPageReqVO reqVO);

    StoreWxpayConfigDO getConfigByStoreId(Long storeId);

    MiniappConfigVO getMiniappConfig(Long tenantId);

}
