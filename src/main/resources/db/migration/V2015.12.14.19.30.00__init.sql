-----------------------------------
-----------  管理员表  -------------
-----------------------------------
CREATE TABLE `edy_admin` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  权限表  ---------------
-----------------------------------
CREATE TABLE `edy_permission` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKCAA5B74A2205709` (`pid`),
  CONSTRAINT `FKCAA5B74A2205709` FOREIGN KEY (`pid`) REFERENCES `edy_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  管理员权限关联表  ------
-----------------------------------
CREATE TABLE `edy_admin_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `admin_id` (`admin_id`,`permission_id`),
  KEY `permission_fk` (`permission_id`),
  CONSTRAINT `admin_fk` FOREIGN KEY (`admin_id`) REFERENCES `edy_admin` (`id`),
  CONSTRAINT `permission_fk` FOREIGN KEY (`permission_id`) REFERENCES `edy_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=209 DEFAULT CHARSET=utf8;
-----------------------------------
-----------  部门表  ---------------
-----------------------------------
CREATE TABLE `edy_department` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  员工表  ---------------
-----------------------------------
CREATE TABLE `edy_employee` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `attendance_cn` varchar(255) NOT NULL DEFAULT '',
  `department_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_attendance` (`attendance_cn`),
  KEY `department_fk` (`department_id`),
  CONSTRAINT `department_fk` FOREIGN KEY (`department_id`) REFERENCES `edy_department` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  请假调休登记表  --------
-----------------------------------
CREATE TABLE `edy_leave` (
  `id` bigint(20) NOT NULL,
  `type` tinyint(2) NOT NULL,
  `begin_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `remark` text,
  `employee_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  考勤记录表  -----------
-----------------------------------
CREATE TABLE `edy_attendance` (
  `id` bigint(20) NOT NULL,
  `work_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `am_time` timestamp NULL DEFAULT NULL,
  `pm_time` timestamp NULL DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employee_fk` (`employee_id`),
  CONSTRAINT `employee_fk` FOREIGN KEY (`employee_id`) REFERENCES `edy_employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-----------------------------------
-----------  许列表  ---------------
-----------------------------------
CREATE TABLE `edy_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;