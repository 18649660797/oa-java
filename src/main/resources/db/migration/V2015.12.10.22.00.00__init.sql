-- ----------------------------------
-- 管理员表
-- ----------------------------------
CREATE TABLE `edy_admin` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------------
-- 权限表
-- ----------------------------------
CREATE TABLE `edy_permission` (
  `id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(255) NOT NULL,
  `label` varchar(255) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- ----------------------------------
-- 管理员、权限关联表
-- ----------------------------------
CREATE TABLE `edy_admin_permission` (
  `id` int(11) NOT NULL DEFAULT '0',
  `admin_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `admin_id` (`admin_id`,`permission_id`),
  KEY `permission_fk` (`permission_id`),
  CONSTRAINT `admin_fk` FOREIGN KEY (`admin_id`) REFERENCES `edy_admin` (`id`),
  CONSTRAINT `permission_fk` FOREIGN KEY (`permission_id`) REFERENCES `edy_permission` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
