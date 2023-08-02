package com.yanzu.module.system.job;

import com.yanzu.framework.quartz.core.handler.JobHandler;
import com.yanzu.framework.tenant.core.job.TenantJob;
import com.yanzu.module.member.api.user.MemberUserApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * app订单检查定时任务，用于自动开始订单，或者自动结束订单
 */
@Component
@TenantJob
public class AppOrderCheckJob implements JobHandler {

    @Resource
    private MemberUserApi memberUserApi;

    @Override
    public String execute(String param) {
        memberUserApi.executeOrderJob();
        return "success";
    }

}
