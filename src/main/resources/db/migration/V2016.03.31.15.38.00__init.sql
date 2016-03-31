INSERT INTO `edy_permission` (`id`, `name`, `label`, `pid`)
VALUES
	(400000, 'permission_leave_type_manager', '考勤类型', 32000);

INSERT INTO `edy_menus` (`id`, `name`, `url`, `level`, `permission_id`, `parent_menus_id`)
VALUES
	(400000, '考勤类型', '/attendance/leave/list', 3, 400000, 32000);
