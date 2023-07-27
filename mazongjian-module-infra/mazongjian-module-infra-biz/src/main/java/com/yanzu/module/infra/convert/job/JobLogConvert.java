package com.yanzu.module.infra.convert.job;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.infra.controller.admin.job.vo.log.JobLogExcelVO;
import com.yanzu.module.infra.controller.admin.job.vo.log.JobLogRespVO;
import com.yanzu.module.infra.dal.dataobject.job.JobLogDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 定时任务日志 Convert
 *
 * @author 芋艿
 */
@Mapper
public interface JobLogConvert {

    JobLogConvert INSTANCE = Mappers.getMapper(JobLogConvert.class);

    JobLogRespVO convert(JobLogDO bean);

    List<JobLogRespVO> convertList(List<JobLogDO> list);

    PageResult<JobLogRespVO> convertPage(PageResult<JobLogDO> page);

    List<JobLogExcelVO> convertList02(List<JobLogDO> list);

}
