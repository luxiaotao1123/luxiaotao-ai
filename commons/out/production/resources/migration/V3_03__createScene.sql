
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 场景表
-- ------
CREATE TABLE `t_scene` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` varchar(512) DEFAULT "" COMMENT '伪Id',
  `key` VARCHAR (512)  DEFAULT "" COMMENT '场景的key值',
  `value` VARCHAR (512)  DEFAULT "" COMMENT '场景的value值',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
    `time_interval` varchar(512) DEFAULT '0' COMMENT '时间间隔',
    `proportion` int(11) DEFAULT 0 COMMENT '达成比例',
  `tips` varchar(512) DEFAULT "" COMMENT '温馨提示',
  `hourly_fee` DOUBLE (11,2) DEFAULT 0 COMMENT '每小时费用',
  `grateful_free`  VARCHAR (512) DEFAULT 0 COMMENT '感谢费',
  `first_start`  int (11) DEFAULT 0 COMMENT '最早开始时间间隔',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT '场景表';
