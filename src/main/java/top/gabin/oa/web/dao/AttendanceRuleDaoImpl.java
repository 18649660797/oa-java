/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceRuleDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.AttendanceRule;
import top.gabin.oa.web.entity.AttendanceRuleImpl;

/**
 * @author linjiabin  on  15/12/21
 */
@Repository("attendanceRuleDao")
public class AttendanceRuleDaoImpl extends CommonBaseDaoImpl<AttendanceRule, AttendanceRuleImpl> implements AttendanceRuleDao {
}
