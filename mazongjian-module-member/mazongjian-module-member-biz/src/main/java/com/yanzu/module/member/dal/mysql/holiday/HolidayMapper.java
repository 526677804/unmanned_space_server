package com.yanzu.module.member.dal.mysql.holiday;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.tenant.core.aop.TenantIgnore;
import com.yanzu.module.member.dal.dataobject.holiday.HolidayDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HolidayMapper extends BaseMapperX<HolidayDo> {

    @TenantIgnore
    HolidayDo isHoliday(@Param("time") String time);
}
