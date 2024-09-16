package com.yanzu.module.member.dal.dataobject.faceblacklist;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 人脸黑名单 DO
 *
 * @author 超级管理员
 */
@TableName("member_face_blacklist")
@KeySequence("member_face_blacklist_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceBlacklistDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long blacklistId;
    /**
     * 用户
     */
    private Long userId;
    /**
     * 门店
     */
    private Long storeId;
    /**
     * 照片 base64编码
     */
    private String photoUrl;
    private String photoData;
    /**
     * 人员guid
     */
    private String admitGuid;
    /**
     * 备注
     */
    private String remark;

}
