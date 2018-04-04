
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 商家用户
-- ------
CREATE TABLE `t_merchant_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` VARCHAR(512) DEFAULT "" COMMENT '伪Id',
  `store_id` VARCHAR(512) DEFAULT "" COMMENT '店铺id',
  `username` VARCHAR(512) DEFAULT "" COMMENT '用户名',
  `password` VARCHAR(512) DEFAULT "" COMMENT '密码',
  `real_name` VARCHAR(512) DEFAULT "" COMMENT '真实姓名',
  `account_num` VARCHAR(512) DEFAULT "" COMMENT '支付宝账号',
  `money` DOUBLE (11,2) DEFAULT 0 COMMENT '余额',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
