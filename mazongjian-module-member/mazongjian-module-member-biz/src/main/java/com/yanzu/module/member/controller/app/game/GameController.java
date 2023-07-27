package com.yanzu.module.member.controller.app.game;

import com.yanzu.framework.common.pojo.CommonResult;
import com.yanzu.framework.common.pojo.PageResult;
import com.yanzu.framework.security.core.annotations.PreAuthenticated;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoReqVO;
import com.yanzu.module.member.controller.app.game.vo.AppGameInfoRespVO;
import com.yanzu.module.member.controller.app.game.vo.AppGamePageReqVO;
import com.yanzu.module.member.controller.app.order.vo.OrderListRespVO;
import com.yanzu.module.member.controller.app.order.vo.OrderPageReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
public class GameController {
    @PostMapping("/save")
    @Operation(summary = "发起在线组局", description = "线上组局使用")
    @PreAuthenticated
    public CommonResult<Boolean> save(@RequestBody @Valid AppGameInfoReqVO reqVO) {
        return null;
    }
    @PostMapping("/getGamePage")
    @Operation(summary = "获取在线组局信息列表", description = "线上组局使用")
    @PreAuthenticated
    public CommonResult<PageResult<AppGameInfoRespVO>> getOrderPage(@RequestBody @Valid AppGamePageReqVO reqVO) {
        return null;
    }

    @DeleteMapping("/deleteUser/{gameId}/{userId}")
    @Operation(summary = "踢出对局的用户", description = "线上组局使用")
    @PreAuthenticated
    public CommonResult<Boolean> deleteUser(@PathVariable("gameId") Long gameId, @PathVariable("userId") Long userId) {
        return null;
    }

    @DeleteMapping("/join/{gameId}")
    @Operation(summary = "加入或退出对局", description = "线上组局使用")
    @PreAuthenticated
    public CommonResult<Boolean> join(@PathVariable("gameId") Long gameId) {
        return null;
    }




}
