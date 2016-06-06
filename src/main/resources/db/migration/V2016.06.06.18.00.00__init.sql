ALTER TABLE `edy_leave` ADD `leave_type_id` INT  NULL  DEFAULT NULL;
ALTER TABLE `edy_leave` ADD FOREIGN KEY (`leave_type_id`) REFERENCES `edy_leave_type` (`ID`);
