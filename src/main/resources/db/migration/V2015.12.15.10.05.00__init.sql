DROP TABLE `edy_attendance`;
CREATE TABLE `edy_attendance` (
  `id` bigint(20) NOT NULL,
  `work_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `am_time` varchar(255) DEFAULT NULL,
  `pm_time` varchar(255) DEFAULT NULL,
  `employee_id` bigint(20) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `employee_fk` (`employee_id`),
  CONSTRAINT `employee_fk` FOREIGN KEY (`employee_id`) REFERENCES `edy_employee` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;