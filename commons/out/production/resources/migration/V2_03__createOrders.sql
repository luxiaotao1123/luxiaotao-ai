
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 订单表
-- ------
CREATE TABLE `t_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) NOT NULL COMMENT '用户Id',
  `uuid` varchar(512)  DEFAULT "" COMMENT '伪Id',
  `start_time`  varchar(512)   DEFAULT "" COMMENT '开始的时间',
  `sel_time_type`  tinyint(1) DEFAULT 0 COMMENT '选择时间类型',
  `order_period` int(6) DEFAULT 0 COMMENT '订单的周期',
  `order_type` varchar(64)  DEFAULT "" COMMENT '订单的类型',
  `person_count` int(4)   DEFAULT 0 COMMENT '人数的数量',
  `grateful_free` double(11,2)  DEFAULT 0  COMMENT '感谢费',
   `user_invitation` varchar(512)DEFAULT "" COMMENT '响应的 邀约Id',
  `label` varchar(512)  DEFAULT ""  COMMENT '个性的标签',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  `perhaps` INT(11) DEFAULT 0 COMMENT '区分是邀约还是应约订单',
  `scenes` INT(11) DEFAULT 0 COMMENT '场景选择',
  `place` varchar(512)  DEFAULT ""  COMMENT '邀约地点',
  `money` double(11,2)  DEFAULT 0  COMMENT '金额',
  `longitude` double(11,3)  DEFAULT 0  COMMENT '经度',
  `latitude` double(11,3)  DEFAULT 0  COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT '订单表';
