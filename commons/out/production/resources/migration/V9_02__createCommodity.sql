
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 商店商品表
-- ------
CREATE TABLE `t_commodity` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `combo_id` VARCHAR (512) DEFAULT "" COMMENT '套餐id',
  `uuid` varchar(512) DEFAULT "" COMMENT '伪Id',
  `name` varchar(512) DEFAULT "" COMMENT '商品名称',
  `price`  DOUBLE (11,2)  DEFAULT 0 COMMENT '套餐价格',
  `size`  INT(11)  DEFAULT 0 COMMENT '商品数量',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
