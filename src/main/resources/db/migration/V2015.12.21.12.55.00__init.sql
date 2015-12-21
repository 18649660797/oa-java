CREATE TABLE `edy_attendance_rule` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `extra_data` varchar(255) NOT NULL DEFAULT '',
  `type` tinyint(1) NOT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `begin_date` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `end_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `edy_attendance_rule_detail` (
  `id` bigint(20) NOT NULL,
  `rule` text NOT NULL,
  `limit` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `edy_attendance_rule_detail_xref` (
  `rule_id` bigint(20) DEFAULT NULL,
  `detail_id` bigint(20) DEFAULT NULL,
  `map_key` varchar(255) NOT NULL DEFAULT '',
  UNIQUE KEY `rule_detail_group_uk` (`rule_id`,`detail_id`),
  KEY `xref_rule_detail_id_fk` (`detail_id`),
  CONSTRAINT `xref_rule_detail_id_fk` FOREIGN KEY (`detail_id`) REFERENCES `edy_attendance_rule_detail` (`id`),
  CONSTRAINT `xref_rule_id_fk` FOREIGN KEY (`rule_id`) REFERENCES `edy_attendance_rule` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;