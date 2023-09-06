package com.yanzu.module.system.dal.mysql.social;

import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yanzu.module.system.dal.dataobject.social.SocialUserBindDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SocialUserBindMapper extends BaseMapperX<SocialUserBindDO> {

    default void deleteByUserIdAndSocialType(Long userId, Integer socialType) {
        delete(new LambdaQueryWrapperX<SocialUserBindDO>().eq(SocialUserBindDO::getUserId, userId).eq(SocialUserBindDO::getSocialType, socialType));
    }

    default void deleteBySocialUserId( Long socialUserId) {
        delete(new LambdaQueryWrapperX<SocialUserBindDO>().eq(SocialUserBindDO::getSocialUserId, socialUserId));
    }

    default SocialUserBindDO selectBySocialUserId( Long socialUserId) {
        return selectOne(SocialUserBindDO::getSocialUserId, socialUserId);
    }

    default List<SocialUserBindDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<SocialUserBindDO>().eq(SocialUserBindDO::getUserId, userId));
    }

    @Select("select su.openid from system_social_user su left join system_social_user_bind sub ON su.id=sub.social_user_id where su.deleted=0 and sub.deleted=0 and sub.user_id=#{userId} and sub.social_type=#{type}")
    String getUserOpenIdByType(@Param("userId") Long userId, @Param("type") Integer type);

}
