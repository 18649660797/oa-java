INSERT INTO `edy_permission` (`id`, `name`, `label`, `pid`)
VALUES
	(4000, 'permission_my_help_top', '自助服务', NULL),
	(41000, 'permission_my_help_tra_manager', '事务登记', 4000),
	(401000, 'permission_my_help_leave_manager', '请假', 41000),
	(402000, 'permission_my_help_out_manager', '外出', 41000);

INSERT INTO `edy_menus` (`id`, `name`, `url`, `level`, `permission_id`, `parent_menus_id`)
VALUES
	(4000, '自助服务', '', 1, 4000, NULL),
	(41000, '事务登记', '', 2, 41000, 4000),
	(401000, '请假', '/help/leave/list', 3, 401000, 41000),
	(402000, '外出', '/help/out/list', 3, 402000, 41000);

