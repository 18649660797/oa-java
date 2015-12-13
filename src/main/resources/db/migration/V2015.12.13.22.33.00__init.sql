-- 部门表
CREATE TABLE `edy_department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- 员工表
CREATE TABLE `edy_employee` (
  `id` BIGINT(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `attendance_cn` varchar(255) NOT NULL DEFAULT '',
  `department_id` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `department_fk` (`department_id`),
  CONSTRAINT `department_fk` FOREIGN KEY (`department_id`) REFERENCES `edy_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;