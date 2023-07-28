package com.yanzu.module.member.convert.user;

import java.util.*;

import com.yanzu.framework.common.pojo.PageResult;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yanzu.module.member.controller.admin.user.vo.*;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;

/**
 * 用户管理 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface AppUserConvert {

    AppUserConvert INSTANCE = Mappers.getMapper(AppUserConvert.class);

    AppUserDO convert(AppUserCreateReqVO bean);

    AppUserDO convert(AppUserUpdateReqVO bean);

    AppUserRespVO convert(AppUserDO bean);

    List<AppUserRespVO> convertList(List<AppUserDO> list);

    PageResult<AppUserRespVO> convertPage(PageResult<AppUserDO> page);

    List<AppUserExcelVO> convertList02(List<AppUserDO> list);

}
