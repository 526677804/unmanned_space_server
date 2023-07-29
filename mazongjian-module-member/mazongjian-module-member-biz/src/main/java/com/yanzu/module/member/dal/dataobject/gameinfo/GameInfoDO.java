package com.yanzu.module.member.dal.dataobject.gameinfo;

import lombok.*;

import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 在线组局管理 DO
 *
 * @author 芋道源码
 */
@TableName("member_game_info")
@KeySequence("member_game_info_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameInfoDO extends BaseDO {

    /**
     * 对局ID
     */
    @TableId
    private Long gameId;
    /**
     * 门店ID
     */
    private Long storeId;
    /**
     * 房间ID
     */
    private Long roomId;
    /**
     * 规则描述
     */
    private String ruleDesc;
    /**
     * 开始时间
     */
    private Date startTime;

    private Date endTime;
    /**
     * 用户id
     */
    private Long userId;

    private Integer userNum;
    /**
     * 玩家ids
     */
    private String playUserIds;
    /**
     * 状态
     * <p>
     * 枚举 {@link TODO member_game_status 对应的类}
     */
    private Integer status;

}
