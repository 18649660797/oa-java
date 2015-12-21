package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.form.EditAttendanceRuleForm;
import top.gabin.oa.web.entity.AttendanceRule;
import top.gabin.oa.web.entity.AttendanceRuleImpl;

import java.util.List;

/**
 * @author linjiabin  on  15/12/21
 */
public interface AttendanceRuleService {
    List<AttendanceRuleImpl> findRulesByMonth(String month);
    AttendanceRule findById(Long id);
    void setAttendanceRule(EditAttendanceRuleForm form);
    void batchDelete(String ids);
}
