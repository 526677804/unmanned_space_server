package com.yanzu.module.infra.api.config;

public interface ConfigApi {

    int updateConfigValue(String k, String v);

    String getConfigValueByKey(String key);

}
