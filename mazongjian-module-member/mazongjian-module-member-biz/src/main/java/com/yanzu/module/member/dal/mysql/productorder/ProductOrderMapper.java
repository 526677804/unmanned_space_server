package com.yanzu.module.member.dal.mysql.productorder;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.member.dal.dataobject.productorder.ProductOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品订单 Mapper
 *
 * @author
 */
@Mapper
public interface ProductOrderMapper extends BaseMapperX<ProductOrderDO> {

    Long selectByOrderNo(String orderNo);

    void updateByOrderNo(String orderNo);

}
