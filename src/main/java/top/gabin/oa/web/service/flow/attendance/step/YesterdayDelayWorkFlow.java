/**
 * Copyright (c) 2015 云智盛世
 * Created with YesterdayDelayWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import org.apache.commons.lang3.StringUtils;
import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.utils.date.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/17
 */
public class YesterdayDelayWorkFlow extends AbstractAnalysisWorkFlow {
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                Attendance yesterday = null;
                List<AnalysisResult> analysisResults = employeeAnalysisResult.getAnalysisResultList();
                for (AnalysisResult analysisResult : analysisResults) {
                    Attendance attendance = analysisResult.getAttendance();
                    if (yesterday != null && StringUtils.isNotBlank(yesterday.getPmTime())) {
                        String pmTime = yesterday.getPmTime();
                        Date pmDate = TimeUtils.parseDate("2015-01-01 " + pmTime + ":00");
                        if (TimeUtils.afterOrEqual(pmDate, TimeUtils.parseDate("2015-01-01 21:30:00"))) {
                            Date amNeedFitTime = analysisResult.getWorkFit();
                            String workDateFormat = attendance.getWorkDateFormat();
                            Date date = TimeUtils.parseDate(workDateFormat + " 10:00:00");
                            if (amNeedFitTime == null || amNeedFitTime.before(date)) {
                                analysisResult.setWorkFit(date);
                                analysisResult.setYesterdayWorkDelay(true);
                            }
                        }
                    }
                    yesterday = attendance;
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
