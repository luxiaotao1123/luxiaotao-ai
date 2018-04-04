
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 订单表
-- ------
CREATE TABLE `t_orders_receive` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT 0 COMMENT '用户Id',
  `uuid` varchar(512) DEFAULT "" COMMENT '伪Id',
  `orders_id` varchar(512) DEFAULT "" COMMENT '订单Id',
  `money` double(11,2) DEFAULT 0 COMMENT '金额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
