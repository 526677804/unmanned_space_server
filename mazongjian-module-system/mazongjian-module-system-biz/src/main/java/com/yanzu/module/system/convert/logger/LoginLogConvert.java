package com.yanzu.module.system.convert.logger;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.yanzu.module.system.controller.admin.logger.vo.loginlog.LoginLogExcelVO;
import com.yanzu.module.system.controller.admin.logger.vo.loginlog.LoginLogRespVO;
import com.yanzu.module.system.dal.dataobject.logger.LoginLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoginLogConvert {

    LoginLogConvert INSTANCE = Mappers.getMapper(LoginLogConvert.class);

    PageResult<LoginLogRespVO> convertPage(PageResult<LoginLogDO> page);

    List<LoginLogExcelVO> convertList(List<LoginLogDO> list);

    LoginLogDO convert(LoginLogCreateReqDTO bean);

}
