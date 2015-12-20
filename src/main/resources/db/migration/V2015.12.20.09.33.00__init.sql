-- 业务配置
CREATE TABLE `edy_business_type` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `key` varchar(255) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `show` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `edy_business_config` (
  `id` bigint(20) NOT NULL,
  `label` varchar(255) NOT NULL,
  `key` varchar(255) NOT NULL,
  `value` varchar(255) NOT NULL,
  `desc` text,
  `required` tinyint(1) NOT NULL DEFAULT '0',
  `type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `business_type_fk` (`type_id`),
  CONSTRAINT `business_type_fk` FOREIGN KEY (`type_id`) REFERENCES `edy_business_type` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;