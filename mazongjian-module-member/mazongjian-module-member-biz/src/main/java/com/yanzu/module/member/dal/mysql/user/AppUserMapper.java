package com.yanzu.module.member.dal.mysql.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.member.controller.admin.user.vo.AppUserExportReqVO;
import com.yanzu.module.member.controller.admin.user.vo.AppUserPageReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameUserListRespVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageReqVO;
import com.yanzu.module.member.controller.app.user.vo.AppMemberPageRespVO;
import com.yanzu.module.member.dal.dataobject.user.AppUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户管理 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface AppUserMapper extends BaseMapperX<AppUserDO> {

    default PageResult<AppUserDO> selectPage(AppUserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AppUserDO>()
                .likeIfPresent(AppUserDO::getNickname, reqVO.getNickname())
                .eqIfPresent(AppUserDO::getStatus, reqVO.getStatus())
                .likeIfPresent(AppUserDO::getMobile, reqVO.getMobile())
                .betweenIfPresent(AppUserDO::getLoginDate, reqVO.getLoginDate())
                .eqIfPresent(AppUserDO::getUserType, reqVO.getUserType())
                .betweenIfPresent(AppUserDO::getMoney, reqVO.getMoney())
                .betweenIfPresent(AppUserDO::getWithdrawalMoney, reqVO.getWithdrawalMoney())
                .betweenIfPresent(AppUserDO::getBalance, reqVO.getBalance())
                .betweenIfPresent(AppUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AppUserDO::getId));
    }

    default List<AppUserDO> selectList(AppUserExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<AppUserDO>()
                .likeIfPresent(AppUserDO::getNickname, reqVO.getNickname())
                .eqIfPresent(AppUserDO::getStatus, reqVO.getStatus())
                .likeIfPresent(AppUserDO::getMobile, reqVO.getMobile())
                .betweenIfPresent(AppUserDO::getLoginDate, reqVO.getLoginDate())
                .eqIfPresent(AppUserDO::getUserType, reqVO.getUserType())
                .betweenIfPresent(AppUserDO::getMoney, reqVO.getMoney())
                .betweenIfPresent(AppUserDO::getWithdrawalMoney, reqVO.getWithdrawalMoney())
                .betweenIfPresent(AppUserDO::getBalance, reqVO.getBalance())
                .betweenIfPresent(AppUserDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AppUserDO::getId));
    }

    List<AppGameUserListRespVO> getInfoByUserIds(String playUserIds);

    IPage<AppMemberPageRespVO> getMemberPage(@Param("page") IPage<AppMemberPageRespVO> page, @Param("reqVO") AppMemberPageReqVO reqVO);

    String getNameById(Long userId);
}
