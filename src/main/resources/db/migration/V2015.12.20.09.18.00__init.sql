-- 配置表
CREATE TABLE `edy_config` (
  `id` bigint(20) NOT NULL,
  `key` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `value` varchar(255) DEFAULT NULL,
  `default_value` varchar(255) NOT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;