
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 商店套餐表
-- ------
CREATE TABLE `t_combo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `store_id` VARCHAR (512) DEFAULT "" COMMENT '店铺id',
  `uuid` varchar(512) DEFAULT "" COMMENT '伪Id',
  `combo` varchar(512) DEFAULT "" COMMENT '套餐名称',
  `price`  DOUBLE (11,2)  DEFAULT 0 COMMENT '套餐价格',
  `privilege`  VARCHAR (512) DEFAULT "" COMMENT '优惠',
  `picture`  VARCHAR (512) DEFAULT "" COMMENT '图片',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
