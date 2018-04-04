
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 商店套餐表
-- ------
CREATE TABLE `t_store` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `maps_id` INT(11) DEFAULT 0 COMMENT '店铺地址id',
  `uuid` VARCHAR(512) DEFAULT "" COMMENT '伪Id',
  `name` VARCHAR(512) DEFAULT "" COMMENT '店铺名称',
  `address` VARCHAR(512) DEFAULT "" COMMENT '店铺地址',
  `banner` VARCHAR(512) DEFAULT "" COMMENT '广告位',
  `pictures` VARCHAR(512) DEFAULT "" COMMENT '图片',
  `phone` VARCHAR(512) DEFAULT "" COMMENT '电话',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
