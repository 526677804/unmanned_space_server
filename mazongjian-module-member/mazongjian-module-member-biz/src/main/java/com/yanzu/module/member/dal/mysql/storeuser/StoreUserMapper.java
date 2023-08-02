package com.yanzu.module.member.dal.mysql.storeuser;

import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserExportReqVO;
import com.yanzu.module.member.controller.admin.storeuser.vo.StoreUserPageReqVO;
import com.yanzu.module.member.controller.app.manager.vo.AppClearUserPageReqVO;
import com.yanzu.module.member.controller.app.manager.vo.AppClearUserPageRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppGiftBalanceListRespVO;
import com.yanzu.module.member.dal.dataobject.storeuser.StoreUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 门店用户管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StoreUserMapper extends BaseMapperX<StoreUserDO> {

    default PageResult<StoreUserDO> selectPage(StoreUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StoreUserDO>()
                .eqIfPresent(StoreUserDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(StoreUserDO::getUserId, reqVO.getUserId())
                .likeIfPresent(StoreUserDO::getName, reqVO.getName())
                .eqIfPresent(StoreUserDO::getType, reqVO.getType())
                .eqIfPresent(StoreUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(StoreUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreUserDO::getId));
    }

    default List<StoreUserDO> selectList(StoreUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StoreUserDO>()
                .eqIfPresent(StoreUserDO::getStoreId, reqVO.getStoreId())
                .eqIfPresent(StoreUserDO::getUserId, reqVO.getUserId())
                .likeIfPresent(StoreUserDO::getName, reqVO.getName())
                .eqIfPresent(StoreUserDO::getType, reqVO.getType())
                .eqIfPresent(StoreUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(StoreUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(StoreUserDO::getId));
    }
    BigDecimal getGiftBalanceByUserId(Long userId);

    List<AppGiftBalanceListRespVO> getGiftBalanceList(Long userId);

    Long checkStorePromission(@Param("storeId") Long storeId, @Param("userId") Long userId, @Param("type") String type);

    List<StoreUserDO> getByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);

    StoreUserDO getByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<StoreUserDO> getByUserId(Long userId);

    List<String> getIdsByUserId(Long userId);

    int deleteClearUser(@Param("userId") Long userId, @Param("storeIds") List<Long> storeIds);
    int deleteClearUserAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<AppClearUserPageRespVO> getClearUserPage(AppClearUserPageReqVO reqVO);
}
