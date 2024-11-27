package com.yanzu.module.member.dal.mysql.storeproduct;

import cn.hutool.core.convert.Convert;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.framework.security.core.util.SecurityFrameworkUtils;
import com.yanzu.module.member.controller.app.storeproduct.vo.StoreProductExportReqVO;
import com.yanzu.module.member.controller.app.storeproduct.vo.StoreProductPageReqVO;
import com.yanzu.module.member.dal.dataobject.storeproduct.StoreProductDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 商品 Mapper
 *
 * @author yshop
 */
@Mapper
public interface StoreProductMapper extends BaseMapperX<StoreProductDO> {

    default PageResult<StoreProductDO> selectPage(StoreProductPageReqVO reqVO) {
        LambdaQueryWrapperX<StoreProductDO> wrapper = new LambdaQueryWrapperX<>();

        wrapper.eq(StoreProductDO::getShopId,reqVO.getStoreId());
        wrapper.likeIfPresent(StoreProductDO::getStoreName, reqVO.getStoreName())
                .likeIfPresent(StoreProductDO::getShopName, reqVO.getShopName())
                .eqIfPresent(StoreProductDO::getIsPostage, reqVO.getIsPostage())
                .eqIfPresent(StoreProductDO::getCateId,reqVO.getCateId())
                .orderByDesc(StoreProductDO::getId);

//        wrapper.eq(StoreProductDO::getIsShow,Convert.toInt(reqVO.getIsShow()));
//        if((Convert.toInt(reqVO.getStock())).equals(0)){
//            wrapper.eq(StoreProductDO::getStock,0);
//        }
//
//        if(CollUtil.isNotEmpty(reqVO.getCatIds())){
//            wrapper.in(StoreProductDO::getCateId,reqVO.getCatIds());
//        }

        return selectPage(reqVO, wrapper);

    }

    default List<StoreProductDO> selectList(StoreProductExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreProductDO>()
                .likeIfPresent(StoreProductDO::getStoreName, reqVO.getStoreName())
                .eqIfPresent(StoreProductDO::getIsPostage, reqVO.getIsPostage())
                .orderByDesc(StoreProductDO::getId));
    }

    @Update("update yshop_store_product set is_show = #{status} where id = #{id}")
    void updateOnsale(@Param("status") Integer status, @Param("id") Long id);


    /**
     * 正常商品库存 加库存 减销量
     * @param num
     * @param productId
     * @return
     */
    @Update("update yshop_store_product set stock=stock+#{num}, sales=sales-#{num}" +
            " where id=#{productId}")
    int incStockDecSales(@Param("num") Integer num,@Param("productId") Long productId);

    /**
     * 正常商品库存 减库存 加销量
     * @param num
     * @param productId
     * @return
     */
    @Update("update yshop_store_product set stock=stock-#{num}, sales=sales+#{num}" +
            " where id=#{productId} and stock >= #{num}")
    int decStockIncSales(@Param("num") Integer num,@Param("productId") Long productId);

}
