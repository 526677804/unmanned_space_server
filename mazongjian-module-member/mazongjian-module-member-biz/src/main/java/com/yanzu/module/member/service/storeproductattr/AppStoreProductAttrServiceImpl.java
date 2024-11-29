package com.yanzu.module.member.service.storeproductattr;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanzu.module.member.controller.app.storeproduct.vo.AppStoreProductAttrQueryVo;
import com.yanzu.module.member.convert.storeproductattr.StoreProductAttrConvert;
import com.yanzu.module.member.dal.dataobject.storeproductattr.StoreProductAttrDO;
import com.yanzu.module.member.dal.dataobject.storeproductattrvalue.StoreProductAttrValueDO;
import com.yanzu.module.member.dal.mysql.storeproductattr.StoreProductAttrMapper;
import com.yanzu.module.member.service.storeproduct.dto.AttrValueDto;
import com.yanzu.module.member.service.storeproductattrvalue.StoreProductAttrValueService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品属性 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class AppStoreProductAttrServiceImpl extends ServiceImpl<StoreProductAttrMapper, StoreProductAttrDO> implements AppStoreProductAttrService {

    @Resource
    private StoreProductAttrValueService storeProductAttrValueService;

    /**
     * 获取商品sku属性
     * @param productId 商品id
     * @return map
     */
    @Override
    public Map<String, Object> getProductAttrDetail(long productId) {
        List<StoreProductAttrDO>  storeProductAttrs = this.baseMapper
                .selectList(Wrappers.<StoreProductAttrDO>lambdaQuery()
                        .eq(StoreProductAttrDO::getProductId,productId)
                        .orderByAsc(StoreProductAttrDO::getId));

        List<StoreProductAttrValueDO>  productAttrValues = storeProductAttrValueService
                .list(Wrappers.<StoreProductAttrValueDO>lambdaQuery()
                        .eq(StoreProductAttrValueDO::getProductId,productId));


        Map<String, StoreProductAttrValueDO> map = productAttrValues.stream()
                .collect(Collectors.toMap(StoreProductAttrValueDO::getSku, p -> p));

        List<AppStoreProductAttrQueryVo> yxStoreProductAttrQueryVoList = new ArrayList<>();

        for (StoreProductAttrDO attr : storeProductAttrs) {
            List<String> stringList = Arrays.asList(attr.getAttrValues().split(","));
            List<AttrValueDto> attrValueDTOS = new ArrayList<AttrValueDto>();
            for (String str : stringList) {
                AttrValueDto attrValueDTO = new AttrValueDto();
                attrValueDTO.setAttr(str);
                attrValueDTOS.add(attrValueDTO);
            }

            AppStoreProductAttrQueryVo attrQueryVo = StoreProductAttrConvert.INSTANCE.convert(attr);
            attrQueryVo.setAttrValue(attrValueDTOS);
            attrQueryVo.setAttrValueArr(stringList);

            yxStoreProductAttrQueryVoList.add(attrQueryVo);
        }

        Map<String, Object> returnMap = new LinkedHashMap<>(2);
        returnMap.put("productAttr",yxStoreProductAttrQueryVoList);
        returnMap.put("productValue",map);

        return returnMap;
    }

}
