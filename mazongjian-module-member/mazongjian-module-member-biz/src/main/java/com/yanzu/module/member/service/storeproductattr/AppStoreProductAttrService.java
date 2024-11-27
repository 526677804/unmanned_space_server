package com.yanzu.module.member.service.storeproductattr;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yanzu.module.member.dal.dataobject.storeproductattr.StoreProductAttrDO;

import java.util.Map;

/**
 * 商品属性 Service 接口
 *
 * @author yshop
 */
public interface AppStoreProductAttrService extends IService<StoreProductAttrDO> {

    /**
     * 获取商品sku属性
     * @param productId 商品id
     * @return map
     */
    Map<String, Object> getProductAttrDetail(long productId);


}
