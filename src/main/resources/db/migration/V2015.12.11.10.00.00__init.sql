-- 修改id为自增长，由于是第三方关系表，没有实体
ALTER TABLE `edy_admin_permission` CHANGE `id` `id` INT(11)  auto_increment  NOT NULL;