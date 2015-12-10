INSERT INTO `edy_admin` (`id`, `name`, `password`)
VALUES (-1,'gabin','a7866023ecac10e3df478ca9567fc64e');
INSERT INTO `edy_permission` (`id`, `name`, `label`, `pid`)
VALUES (-1,'super','超级权限',NULL);
INSERT INTO `edy_admin_permission` (`id`, `admin_id`, `permission_id`)
VALUES (1,-1,-1);