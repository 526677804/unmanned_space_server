package com.yanzu.module.system.dal.mysql.social;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yanzu.framework.mybatis.core.mapper.BaseMapperX;
import com.yanzu.module.system.dal.dataobject.social.SocialUserDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SocialUserMapper extends BaseMapperX<SocialUserDO> {

    default SocialUserDO selectByTypeAndCodeAnState(Integer type, String code, String state) {
        return selectOne(new LambdaQueryWrapper<SocialUserDO>()
                .eq(SocialUserDO::getType, type)
                .eq(SocialUserDO::getCode, code)
                .eq(SocialUserDO::getState, state));
    }

    default SocialUserDO selectByTypeAndOpenid(Integer type, String openid) {
        return selectOne(new LambdaQueryWrapper<SocialUserDO>()
                .eq(SocialUserDO::getType, type)
                .eq(SocialUserDO::getOpenid, openid));
    }

    @Select("select concat(miniapp_id,'-',miniapp_secret) from system_tenant where id=#{tenantId}")
    String selectSourceByTenant(Long tenantId);

}
