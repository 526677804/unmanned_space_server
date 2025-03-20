
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;


ALTER TABLE `member_room_info` ADD COLUMN `pre_price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '预付费价格' AFTER `svg_y`;

ALTER TABLE `member_room_info` ADD COLUMN `pre_unit` int NULL DEFAULT 1 COMMENT '预付费单位' AFTER `pre_price`;

ALTER TABLE `member_room_info` ADD COLUMN `min_charge` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '最低消费价格' AFTER `pre_unit`;

ALTER TABLE `member_store_user` ADD COLUMN `total_score` int NULL DEFAULT 0 COMMENT '总积分' AFTER `add_time`;

ALTER TABLE `member_store_user` ADD COLUMN `vip_level` int NULL DEFAULT 0 COMMENT 'vip等级' AFTER `total_score`;

CREATE TABLE `member_store_vip_config`  (
  `vip_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `store_id` bigint NOT NULL COMMENT '门店ID',
  `vip_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会员名称',
  `vip_level` tinyint NOT NULL COMMENT '等级 从0开始',
  `vip_discount` tinyint UNSIGNED NOT NULL DEFAULT 99 COMMENT '折扣 88=8.8折',
  `score` int UNSIGNED NOT NULL COMMENT '积分门槛',
  `creator` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户编号',
  PRIMARY KEY (`vip_id`) USING BTREE,
  INDEX `store_id`(`store_id` ASC) USING BTREE,
  INDEX `store_id_2`(`store_id` ASC, `vip_level` ASC, `deleted` ASC, `tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '门店会员配置表' ROW_FORMAT = Dynamic;



update member_pkg_info set room_type=null where room_type=0;



UPDATE member_store_info
SET expire_time = DATE_ADD(create_time, INTERVAL 1 YEAR)
WHERE expire_time IS NULL AND create_time IS NOT NULL;


ALTER TABLE `member_face_blacklist` MODIFY COLUMN `photo_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '照片 base64编码' AFTER `photo_url`;

ALTER TABLE `member_face_record` MODIFY COLUMN `photo_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '照片 base64编码' AFTER `photo_url`;



SET FOREIGN_KEY_CHECKS=1;