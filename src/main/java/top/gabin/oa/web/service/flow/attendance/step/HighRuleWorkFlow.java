/**
 * Copyright (c) 2015 云智盛世
 * Created with HighRuleWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceRule;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.service.AttendanceRuleService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 高级规则处理
 * @author linjiabin  on  15/12/21
 */
public class HighRuleWorkFlow extends AbstractAnalysisWorkFlow {
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> analysisResults) {
        for (DepartmentAnalysisResult departmentAnalysisResult : analysisResults) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    List<AttendanceRule> attendanceRuleList = analysisResult.getAttendanceRuleList();
                    String workDateFormat = analysisResult.getAttendance().getWorkDateFormat();
                    for (AttendanceRule rule : attendanceRuleList) {
                        String extraData = rule.getExtraData();
                        switch (rule.getType()) {
                            case WORK_FIT:
                                analysisResult.setWorkFit(TimeUtils.parseDate(workDateFormat + " " + extraData));
                                break;
                            case LEAVE_FIT:
                                analysisResult.setLeaveFit(TimeUtils.parseDate(workDateFormat + " " + extraData));
                                break;
                            case WORK_UN_FIT:
                                analysisResult.setWorkNeedFit(false);
                                break;
                            case LEAVE_UN_FIT:
                                analysisResult.setLeaveNeedFit(false);
                                break;
                        }
                    }
                }
            }
        }
        return analysisResults;
    }

}
