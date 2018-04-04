
SET FOREIGN_KEY_CHECKS=0;



-- ------
-- 地图表
-- ------
-- CREATE TABLE `t_map` (
--   `id` int(11) NOT NULL,
--   `uuid` varchar(255) DEFAULT NULL,
--    pt point not null,
--   `province` varchar(20) NOT NULL,
--   `city` varchar(20) NOT NULL,
--   `longitude` double(10,3) NOT NULL,
--   `latitude` double(10,3) NOT NULL,
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `t_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(215)  DEFAULT ""  COMMENT '地点名称',
  `phone` varchar(128)  DEFAULT "" COMMENT '商家电话',
  `scene` varchar(20)  DEFAULT ""  COMMENT '场景类型的名称',
  `province` varchar(20)  DEFAULT ""  COMMENT '省',
  `city` varchar(20) DEFAULT "" COMMENT '市',
  `district` varchar(20)  DEFAULT ""  COMMENT '区',
  `address` varchar(215) DEFAULT "" COMMENT '街道信息',
  `longitude` double(9,6)  DEFAULT 0 COMMENT '经度',
  `latitude` double(8,6)  DEFAULT 0 COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

