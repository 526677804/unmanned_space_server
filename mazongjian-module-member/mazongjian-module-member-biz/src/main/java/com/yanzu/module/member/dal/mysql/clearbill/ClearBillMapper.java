package com.yanzu.module.member.dal.mysql.clearbill;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.controller.app.clear.vo.AppClearBillReqVO;
import com.yanzu.module.member.controller.app.clear.vo.AppClearBillRespVO;
import com.yanzu.module.member.dal.dataobject.clearbill.ClearBillDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保洁账单管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ClearBillMapper extends BaseMapperX<ClearBillDO> {

    BigDecimal sumMoneyByUserId(Long userId);

    IPage<AppClearBillRespVO> getClearBillPage(@Param("page") IPage<AppClearBillRespVO> page, @Param("reqVO") AppClearBillReqVO reqVO);
}
