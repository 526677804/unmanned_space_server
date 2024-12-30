package com.yanzu.module.member.dal.mysql.productorder;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.tenant.core.aop.TenantIgnore;
import com.yanzu.module.member.controller.app.productorder.vo.AppHaveOrderStoreRespVO;
import com.yanzu.module.member.dal.dataobject.productorder.ProductOrderDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品订单 Mapper
 *
 * @author
 */
@Mapper
public interface ProductOrderMapper extends BaseMapperX<ProductOrderDO> {

    @TenantIgnore
    ProductOrderDO selectByOrderNo(String orderNo);

    void updateByOrderNo(String orderNo);

    List<AppHaveOrderStoreRespVO> selectHaveOrderStore(Long userId);

    String getPhone(Long orderId);
}
