package com.yanzu.module.member.controller.app.game;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.idempotent.core.annotation.Idempotent;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoRespVO;
import com.yanzu.module.member.controller.app.game.vo.AppGamePageReqVO;
import com.yanzu.module.member.service.game.AppGameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.concurrent.TimeUnit;

import static com.yanzu.framework.common.pojo.CommonResult.success;

/**
 * @PACKAGE_NAME: com.yanzu.module.member.controller.app.game
 * @DESCRIPTION:
 * @USER: MrGuan  mrguan@aliyun.com
 * @DATE: 2023/7/26 17:00
 */
@Tag(name = "miniapp -在线组局")
@RestController
@RequestMapping("/member/game")
@Validated
@Slf4j
public class AppGameController {

    @Resource
    private AppGameService appGameService;


    @PostMapping("/save")
    @Operation(summary = "发起在线组局", description = "线上组局使用")
    @PreAuthenticated
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> save(@RequestBody @Valid AppGameInfoReqVO reqVO) {
        appGameService.save(reqVO);
        return success(true);
    }

    @PostMapping("/getGamePage")
    @Operation(summary = "获取在线组局信息列表", description = "线上组局使用")
    @PreAuthenticated
    public CommonResult<PageResult<AppGameInfoRespVO>> getOrderPage(@RequestBody @Valid AppGamePageReqVO reqVO) {
        return success(appGameService.getOrderPage(reqVO));
    }

    @PostMapping("/deleteUser/{gameId}/{userId}")
    @Operation(summary = "踢出对局的用户", description = "线上组局使用")
    @PreAuthenticated
    @Parameter(name = "gameId")
    @Parameter(name = "userId")
    public CommonResult<Boolean> deleteUser(@PathVariable("gameId") Long gameId, @PathVariable("userId") Long userId) {
        appGameService.deleteUser(gameId, userId);
        return success(true);
    }

    @PostMapping("/join/{gameId}")
    @Operation(summary = "加入或退出对局", description = "线上组局使用")
    @PreAuthenticated
    @Parameter(name = "gameId")
    @Idempotent(timeout = 3, timeUnit = TimeUnit.SECONDS, message = "你的点击太快啦~")
    public CommonResult<Boolean> join(@PathVariable("gameId") Long gameId) {
        appGameService.join(gameId);
        return success(true);
    }
}
