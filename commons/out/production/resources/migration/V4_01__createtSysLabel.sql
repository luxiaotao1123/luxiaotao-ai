
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 场景表
-- ------
CREATE TABLE `t_sys_label` (
  `key` varchar(32) CHARACTER SET utf8mb4 NOT NULL,
  `value` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  `time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `status` int(11) unsigned NOT NULL COMMENT '1 有效 0无效',
  `note` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '备注信息',
  `explain` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ;
