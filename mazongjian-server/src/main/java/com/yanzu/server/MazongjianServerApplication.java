package com.yanzu.server;

import com.dtflys.forest.springboot.annotation.ForestScan;
import com.yanzu.module.member.forest.IotClient;
import com.yanzu.module.member.service.iot.IotDeviceService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.TimeZone;

/**
 * 项目的启动类
 * <p>
 *
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${mazongjian.info.base-package}
@SpringBootApplication(scanBasePackages = {"${mazongjian.info.base-package}.server", "${mazongjian.info.base-package}.module"})
@ForestScan(basePackages = "com.yanzu.module.member.forest")
public class MazongjianServerApplication {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
        SpringApplication.run(MazongjianServerApplication.class, args);
//        new SpringApplicationBuilder(MazongjianServerApplication.class)
//                .applicationStartup(new BufferingApplicationStartup(20480))
//                .run(args);

    }


    @Resource
    private IotDeviceService iotDeviceService;

    //启动成功后 向iot平台上报一下信息
    @Bean
    public CommandLineRunner run() {
        return args -> {
            iotDeviceService.online();
        };
    }
}
