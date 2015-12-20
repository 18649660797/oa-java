-- 增加约束
ALTER TABLE `edy_leave` ADD CONSTRAINT `leave_employee_fk` FOREIGN KEY (`employee_id`) REFERENCES `edy_employee` (`id`);
-- 增加字段和索引
ALTER TABLE `edy_attendance` ADD `work_date_format` VARCHAR(255)  NOT NULL  AFTER `status`;
ALTER TABLE `edy_attendance` ADD INDEX `attendance_work_date_format_idx` (`work_date_format`);
-- 增加唯一索引
ALTER TABLE `edy_attendance` ADD UNIQUE INDEX `work_date_employee_id_group_uk` (`work_date_format`, `employee_id`);