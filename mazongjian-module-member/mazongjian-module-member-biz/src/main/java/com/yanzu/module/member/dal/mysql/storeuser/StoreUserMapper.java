package com.yanzu.module.member.dal.mysql.storeuser;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
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


    BigDecimal getGiftBalanceByUserId(Long userId);

    List<AppGiftBalanceListRespVO> getGiftBalanceList(Long userId);

    Long checkStorePromission(@Param("storeId") Long storeId, @Param("userId") Long userId, @Param("type") String type);

    List<StoreUserDO> getByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);

    StoreUserDO getByUserIdAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<StoreUserDO> getByUserId(Long userId);

    List<Long> getByIdsUserId(Long userId);

    int deleteClearUser(@Param("userId") Long userId, @Param("storeIds") List<Long> storeIds);
    int deleteClearUserAndStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);

    List<AppClearUserPageRespVO> getClearUserPage(AppClearUserPageReqVO reqVO);
}
