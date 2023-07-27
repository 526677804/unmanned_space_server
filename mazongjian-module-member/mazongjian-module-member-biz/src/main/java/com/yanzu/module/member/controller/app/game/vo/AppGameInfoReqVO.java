package com.yanzu.module.member.controller.app.game.vo;

import com.yanzu.module.member.controller.admin.gameinfo.vo.GameInfoBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

@Schema(description = "miniapp - 保存组局信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppGameInfoReqVO {


    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32343")
    private Long storeId;

    @Schema(description = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "14759")
    private Long roomId;

    @Schema(description = "规则描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ruleDesc;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startTime;

}
