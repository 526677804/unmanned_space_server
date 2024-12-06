package com.yanzu.module.member.controller.app.meituanreserve;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.module.member.controller.app.meituanreserve.vo.StoreRuleReqVo;
import com.yanzu.module.member.service.iotreserve.IotRespService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "美团预定")
@RestController
@RequestMapping("/reserve")
@Validated
public class MeiTuanReserveController {

    @Resource
    private IotRespService reserveService;

    @PostMapping("/push/rule")
    public CommonResult pushRule(@RequestBody StoreRuleReqVo storeRuleReqVo){
        return reserveService.pushRule(storeRuleReqVo);
    }

}
