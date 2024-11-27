package com.yanzu.module.member.service.storeproductreply;


import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproduct.vo.AppStoreProductReplyQueryVo;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyPageReqVO;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyUpdateReqVO;
import com.yanzu.module.member.dal.dataobject.storeproductreply.StoreProductReplyDO;

import javax.validation.Valid;

/**
 * 评论 Service 接口
 *
 * @author yshop
 */
public interface StoreProductReplyService {


    /**
     * 更新评论
     *
     * @param updateReqVO 更新信息
     */
    void updateStoreProductReply(@Valid StoreProductReplyUpdateReqVO updateReqVO);

    /**
     * 删除评论
     *
     * @param id 编号
     */
    void deleteStoreProductReply(Long id);

    /**
     * 获得评论
     *
     * @param id 编号
     * @return 评论
     */
    StoreProductReplyDO getStoreProductReply(Long id);


    /**
     * 获得评论分页
     *
     * @param pageReqVO 分页查询
     * @return 评论分页
     */
    PageResult<AppStoreProductReplyQueryVo> getStoreProductReplyPage(StoreProductReplyPageReqVO pageReqVO);



}
