package com.yanzu.module.member.service.groupPay;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoExportReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoPageReqVO;
import com.yanzu.module.member.controller.admin.groupPay.vo.GroupPayInfoRespVO;
import com.yanzu.module.member.dal.dataobject.groupPay.GroupPayInfoDO;
import com.yanzu.module.member.service.iot.groupPay.IotGroupPayPrepareRespVO;

import java.util.Collection;
import java.util.List;

/**
 * 团购支付信息 Service 接口
 *
 * @author MrGuan
 */
public interface GroupPayInfoService {


    /**
     * 获得团购支付信息
     *
     * @param id 编号
     * @return 团购支付信息
     */
    GroupPayInfoDO getGroupPayInfo(Long id);

    /**
     * 获得团购支付信息列表
     *
     * @param ids 编号
     * @return 团购支付信息列表
     */
    List<GroupPayInfoDO> getGroupPayInfoList(Collection<Long> ids);

    /**
     * 获得团购支付信息分页
     *
     * @param pageReqVO 分页查询
     * @return 团购支付信息分页
     */
    PageResult<GroupPayInfoRespVO> getGroupPayInfoPage(GroupPayInfoPageReqVO pageReqVO);

    /**
     * 获得团购支付信息列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 团购支付信息列表
     */
    List<GroupPayInfoDO> getGroupPayInfoList(GroupPayInfoExportReqVO exportReqVO);


    IotGroupPayPrepareRespVO prepare(Long storeId, String ticketNo);


    GroupPayInfoDO consume(Long storeId, String ticketNo, IotGroupPayPrepareRespVO vo);


    void revoke(Long storeId, Integer groupPayType, String ticketNo, String ticketInfo);

}
