CREATE TABLE `edy_leave_type` (
  `ID` int(11) NOT NULL,
  `LABEL` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `TYPE` varchar(100) NOT NULL DEFAULT 'UN_PAID_LEAVE' COMMENT '类型',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `edy_leave_type` (`ID`, `LABEL`, `TYPE`)
VALUES
	(1, '事假', 'UN_PAID_LEAVE'),
	(2, '调休', 'PAID_LEAVE'),
	(3, '病假', 'PAID_LEAVE'),
	(4, '外出', 'PAID_LEAVE'),
	(5, '产假', 'PAID_LEAVE'),
	(6, '年假', 'PAID_LEAVE'),
	(7, '丧假', 'PAID_LEAVE'),
	(8, '陪产假', 'PAID_LEAVE');
