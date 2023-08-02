package com.yanzu.module.member.convert.storeuser;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.storeuser.vo.*;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;

/**
 * 门店用户管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreUserConvert {

    StoreUserConvert INSTANCE = Mappers.getMapper(StoreUserConvert.class);

    StoreUserDO convert(StoreUserCreateReqVO bean);

    StoreUserDO convert(StoreUserUpdateReqVO bean);

    StoreUserRespVO convert(StoreUserDO bean);

    List<StoreUserRespVO> convertList(List<StoreUserDO> list);

    PageResult<StoreUserRespVO> convertPage(PageResult<StoreUserDO> page);

    List<StoreUserExcelVO> convertList02(List<StoreUserDO> list);

}
