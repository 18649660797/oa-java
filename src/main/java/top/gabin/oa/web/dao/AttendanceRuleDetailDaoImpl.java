/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleDetailDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.AttendanceRuleDetail;
import top.gabin.oa.web.entity.AttendanceRuleDetailImpl;

/**
 * @author linjiabin  on  15/12/21
 */
@Repository("attendanceRuleDetailDao")
public class AttendanceRuleDetailDaoImpl extends CommonBaseDaoImpl<AttendanceRuleDetail, AttendanceRuleDetailImpl> implements AttendanceRuleDetailDao {
}
