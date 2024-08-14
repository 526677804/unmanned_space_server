package com.yanzu.module.member.dal.mysql.storesound;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.store.vo.AppStoreSoundInfoRespVO;
import com.yanzu.module.member.dal.dataobject.storesound.StoreSoundInfoDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoreSoundInfoMapper extends BaseMapperX<StoreSoundInfoDO> {

    StoreSoundInfoDO getByStoreId(Long storeId);


    AppStoreSoundInfoRespVO getStoreSoundInfo(Long storeId);
}
