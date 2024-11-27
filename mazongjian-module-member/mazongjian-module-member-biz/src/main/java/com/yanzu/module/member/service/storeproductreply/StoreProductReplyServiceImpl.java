package com.yanzu.module.member.service.storeproductreply;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.module.member.controller.app.storeproduct.vo.AppStoreProductReplyQueryVo;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyPageReqVO;
import com.yanzu.module.member.controller.app.storeproductreply.vo.StoreProductReplyUpdateReqVO;
import com.yanzu.module.member.convert.storeproductreply.StoreProductReplyConvert;
import com.yanzu.module.member.dal.dataobject.storeproductreply.StoreProductReplyDO;
import com.yanzu.module.member.dal.mysql.storeproductreply.StoreProductReplyMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static com.yanzu.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yanzu.module.system.enums.ErrorCodeConstants.STORE_PRODUCT_REPLY_NOT_EXISTS;


/**
 * 评论 Service 实现类
 *
 * @author yshop
 */
@Service
@Validated
public class StoreProductReplyServiceImpl implements StoreProductReplyService {

    @Resource
    private StoreProductReplyMapper storeProductReplyMapper;


    @Override
    public void updateStoreProductReply(StoreProductReplyUpdateReqVO updateReqVO) {
        // 校验存在
        validateStoreProductReplyExists(updateReqVO.getId());
        // 更新
        StoreProductReplyDO updateObj = StoreProductReplyConvert.INSTANCE.convert(updateReqVO);
        storeProductReplyMapper.updateById(updateObj);
    }

    @Override
    public void deleteStoreProductReply(Long id) {
        // 校验存在
        validateStoreProductReplyExists(id);
        // 删除
        storeProductReplyMapper.deleteById(id);
    }

    private void validateStoreProductReplyExists(Long id) {
        if (storeProductReplyMapper.selectById(id) == null) {
            throw exception(STORE_PRODUCT_REPLY_NOT_EXISTS);
        }
    }

    @Override
    public StoreProductReplyDO getStoreProductReply(Long id) {
        return storeProductReplyMapper.selectById(id);
    }

    @Override
    public PageResult<AppStoreProductReplyQueryVo> getStoreProductReplyPage(StoreProductReplyPageReqVO pageReqVO) {
                Page<StoreProductReplyDO> pageModel = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
        List<AppStoreProductReplyQueryVo> list = storeProductReplyMapper
                .allReplyList(pageModel,pageReqVO.getNickname());
        return new PageResult<>(list, storeProductReplyMapper.allReplyListCount(pageReqVO.getNickname()));
    }


}
