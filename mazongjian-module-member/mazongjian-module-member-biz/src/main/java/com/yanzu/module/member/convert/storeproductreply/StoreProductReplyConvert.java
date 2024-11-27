package com.yanzu.module.member.convert.storeproductreply;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyRespVO;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyUpdateReqVO;
import com.yanzu.module.member.dal.dataobject.storeproductreply.StoreProductReplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 评论 Convert
 *
 * @author yshop
 */
@Mapper
public interface StoreProductReplyConvert {

    StoreProductReplyConvert INSTANCE = Mappers.getMapper(StoreProductReplyConvert.class);


    StoreProductReplyDO convert(StoreProductReplyUpdateReqVO bean);

    StoreProductReplyRespVO convert(StoreProductReplyDO bean);

    List<StoreProductReplyRespVO> convertList(List<StoreProductReplyDO> list);

    PageResult<StoreProductReplyRespVO> convertPage(PageResult<StoreProductReplyDO> page);


}
