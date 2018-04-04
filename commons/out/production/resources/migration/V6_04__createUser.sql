
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 用户表
-- ------
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `uuid` varchar(512) DEFAULT "" COMMENT '伪Id',
  `nick_name` varchar(512) DEFAULT "" COMMENT '昵称',
   `gender` varchar(512) DEFAULT "" COMMENT '性别',
  `phone_num` varchar(512) DEFAULT "" COMMENT '手机号',
  `load_name` varchar(512) DEFAULT "" COMMENT '用户的登录名称',
  `load_pass` varchar(512) DEFAULT "" COMMENT '用户的登录密码',
   `money` DOUBLE (11,2) DEFAULT 0 COMMENT '钱包',
  `icon` varchar(512) DEFAULT "" COMMENT '头像',
  `picture` varchar(512) DEFAULT "" COMMENT '图片',
   `age` int(11) DEFAULT 0  COMMENT '年龄',
   `user_grade` int(11) DEFAULT 0  COMMENT '邀请码',
    `occupation` varchar(512) DEFAULT "" COMMENT '职业',
    `height` varchar(512) DEFAULT "" COMMENT '身高',
    `weight` varchar(512) DEFAULT "" COMMENT '体重',
    `city` varchar(512) DEFAULT "" COMMENT '城市',
    `zodiac` varchar(512) DEFAULT "" COMMENT '星座',
    `real_name` varchar(512) DEFAULT "" COMMENT '真实姓名',
    `account_num` varchar(512) DEFAULT "" COMMENT '支付宝账号',
    `label` varchar(512) DEFAULT "" COMMENT '标签',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
