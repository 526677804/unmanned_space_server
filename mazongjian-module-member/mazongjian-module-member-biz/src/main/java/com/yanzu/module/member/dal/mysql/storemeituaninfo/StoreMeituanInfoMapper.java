package com.yanzu.module.member.dal.mysql.storemeituaninfo;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.storemeituaninfo.StoreMeituanInfoDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 门店美团平台信息 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreMeituanInfoMapper extends BaseMapperX<StoreMeituanInfoDO> {


    StoreMeituanInfoDO getByStoreId(Long storeId);
}
