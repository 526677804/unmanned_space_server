package com.yanzu.module.member.dal.dataobject.facerecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yanzu.framework.mybatis.core.dataobject.BaseDO;

/**
 * 人脸识别记录 DO
 *
 * @author 超级管理员
 */
@TableName("member_face_record")
@KeySequence("member_face_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceRecordDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 识别记录ID
     */
    private String faceId;

    /**
     * 门店id
     */
    private Long storeId;
    /**
     * 人员guid
     */
    private String admitGuid;
    /**
     * 照片 base64编码
     */
    private String photoUrl;
    private String photoData;
    /**
     * 识别时间
     */
    private Date showTime;
    /**
     * 比对结果
     */
    private Integer type;
    /**
     * 设备编号
     */
    private String deviceSn;

}
