
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 商城账单
-- ------
CREATE TABLE `t_store_orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` VARCHAR(512) DEFAULT "" COMMENT '伪Id',
  `combo_id` VARCHAR(512) DEFAULT "" COMMENT '套餐id',
  `user_id` VARCHAR(512) DEFAULT "" COMMENT '用户Id',
  `merchant_id` VARCHAR(512) DEFAULT "" COMMENT '商家id',
  `money` DOUBLE (11,2) DEFAULT 0 COMMENT '余额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
