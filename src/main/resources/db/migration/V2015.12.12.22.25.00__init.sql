INSERT INTO `edy_permission` (`id`, `name`, `label`, `pid`) VALUES
(1000,'permission_admin','管理员设置',NULL),
(1001,'permission_admin_list','查看',1000),
(1002,'permission_admin_edit','编辑',1000),
(1003,'permission_admin_delete','删除',1000),
(2000,'permission_employee','员工管理',NULL),
(3000,'permission_attendance','考勤管理',NULL),
(4000,'permission_exception','行政管理',NULL);