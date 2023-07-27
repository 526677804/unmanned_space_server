package com.yanzu.module.member.controller.app.game.vo;

import com.yanzu.module.member.controller.admin.gameinfo.vo.GameInfoBaseVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Schema(description = "miniapp - 组局信息 Response VO")
@Data
@ToString(callSuper = true)
public class AppGameInfoRespVO {

    @Schema(description = "对局ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31151")
    private Long gameId;

    @Schema(description = "门店ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32343")
    private Long storeId;

    @Schema(description = "纬度")
     private Double lat;

    @Schema(description = "经度")
     private Double lon;

    @Schema(description = "详细地址")
    private String address;
    @Schema(description = "门店名称", example = "32343")
    private String storeName;

    @Schema(description = "房间ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long roomId;

    @Schema(description = "房间名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "14759")
    private String roomName;

    @Schema(description = "房间类型  值见枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @Schema(description = "规则描述", requiredMode = Schema.RequiredMode.REQUIRED)
    private String ruleDesc;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startTime;

    @Schema(description = "用户id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26195")
    private Long userId;

    @Schema(description = "状态", example = "2")
    private Integer status;

    @Schema(description = "玩家信息list")
    private List<AppGameUserListRespVO> playUserList;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

}
