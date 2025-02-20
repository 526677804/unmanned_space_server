package com.yanzu.module.member.service.storevipconfig;


import com.yanzu.module.member.controller.app.store.vo.AppEditMemberVipReqVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigListRespVO;
import com.yanzu.module.member.controller.app.store.vo.AppStoreVipConfigSaveReqVO;

import java.util.List;

/**
 * 门店会员配置 Service 接口
 *
 * @author 超级管理员
 */
public interface StoreVipConfigService {

    List<AppStoreVipConfigListRespVO> getVipConfig(Long storeId);

    void saveVipConfig(List<AppStoreVipConfigSaveReqVO> reqVO,Long storeId);

    void editMemberVip(AppEditMemberVipReqVO reqVO);


}
