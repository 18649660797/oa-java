-- 增加唯一索引
ALTER TABLE `edy_business_type` ADD INDEX `business_type_key_uk` (`key`);
-- 配置考勤基准数据
INSERT INTO `edy_business_config` (`id`, `label`, `key`, `value`, `desc`, `required`, `type_id`)
VALUES
	(1000,'上班打卡时间','WORK_FIT','09:00:00','考勤基准配置，上班打卡时间',1,1000),
	(1001,'下班打卡时间','LEAVE_FIT','18:00:00','考勤基准配置，下班打卡时间',1,1000),
	(1002,'中午休息开始时间','REST_BEGIN','12:00:00','考勤基准配置，中午休息开始时间',1,1000),
	(1003,'中午休息结束时间','REST_END','13:30:00','考勤基准配置，中午休息结束时间',1,1000);