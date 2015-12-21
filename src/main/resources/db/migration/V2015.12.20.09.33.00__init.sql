-- 业务配置
CREATE TABLE `edy_business_type` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `type_key` varchar(255) NOT NULL DEFAULT '',
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `is_show` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `business_type_key_uk` (`type_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `edy_business_config` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `config_key` varchar(255) NOT NULL DEFAULT '',
  `config_value` varchar(255) NOT NULL DEFAULT '',
  `description` text,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `business_type_fk` (`type_id`),
  CONSTRAINT `business_type_fk` FOREIGN KEY (`type_id`) REFERENCES `edy_business_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `edy_business_type` (`id`, `label`, `type_key`, `is_show`, `status`)
VALUES (1000, '考勤基准数据', 'attendance_config', 1, 1);
-- 配置考勤基准数据
INSERT INTO `edy_business_config` (`id`, `label`, `config_key`, `config_value`, `description`, `required`, `type_id`)
VALUES
	(1000,'上班打卡时间','WORK_FIT','09:00:00','考勤基准配置，上班打卡时间',1,1000),
	(1001,'下班打卡时间','LEAVE_FIT','18:00:00','考勤基准配置，下班打卡时间',1,1000),
	(1002,'中午休息开始时间','REST_BEGIN','12:00:00','考勤基准配置，中午休息开始时间',1,1000),
	(1003,'中午休息结束时间','REST_END','13:30:00','考勤基准配置，中午休息结束时间',1,1000),
	(1004,'上班打卡偏移时间','WORK_FIT_OFFSET','3','考勤基准配置，上班打卡偏移时间',1,1000);