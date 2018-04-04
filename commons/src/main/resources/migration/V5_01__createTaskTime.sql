
SET FOREIGN_KEY_CHECKS=0;


-- ------
-- 时间定时任务的类
-- ------
CREATE TABLE `t_time_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `schedule_task_index` varchar(512) DEFAULT NULL COMMENT '任务的索引',
  `time_task_name` varchar(512) NOT NULL COMMENT '任务名称',
  `execute_time` varchar(512) NOT NULL COMMENT '执行时间',
  `params` varchar(512) NOT NULL COMMENT '执行时间',
  `uuid` varchar(512) DEFAULT NULL COMMENT '伪Id',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) DEFAULT FALSE COMMENT '是否删除 true是 false不是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


