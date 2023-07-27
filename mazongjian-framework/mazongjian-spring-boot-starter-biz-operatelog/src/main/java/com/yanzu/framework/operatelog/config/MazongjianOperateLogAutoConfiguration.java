package com.yanzu.framework.operatelog.config;

import com.yanzu.framework.operatelog.core.aop.OperateLogAspect;
import com.yanzu.framework.operatelog.core.service.OperateLogFrameworkService;
import com.yanzu.framework.operatelog.core.service.OperateLogFrameworkServiceImpl;
import com.yanzu.module.system.api.logger.OperateLogApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MazongjianOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

    @Bean
    public OperateLogFrameworkService operateLogFrameworkService(OperateLogApi operateLogApi) {
        return new OperateLogFrameworkServiceImpl(operateLogApi);
    }

}
