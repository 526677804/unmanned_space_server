package com.yanzu.module.infra.api.config;

import com.yanzu.module.infra.dal.dataobject.config.ConfigDO;
import com.yanzu.module.infra.dal.mysql.config.ConfigMapper;
import com.yanzu.module.infra.service.config.ConfigService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * @PACKAGE_NAME: com.yanzu.module.infra.api.config
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/11/24 13:33
 */
@Service
@Validated
public class ConfigApiImpl implements ConfigApi {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private ConfigMapper configMapper;
    @Resource
    private ConfigService configService;

    @Override
    public int updateConfigValue(String k, String v) {
        //更新redis缓存
        stringRedisTemplate.opsForValue().set(k, v);
        return configMapper.updateConfigValue(k, v);
    }

    @Override
    public String getConfigValueByKey(String key) {
        ConfigDO config = configService.getConfigByKey(key);
        return config != null ? config.getValue() : null;
    }

}
