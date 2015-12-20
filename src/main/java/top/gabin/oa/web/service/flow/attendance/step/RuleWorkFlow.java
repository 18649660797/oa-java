/**
 * Copyright (c) 2015 云智盛世
 * Created with RuleWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import org.apache.commons.lang3.StringUtils;
import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.service.AttendanceService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author linjiabin  on  15/12/17
 */
public class RuleWorkFlow extends AbstractAnalysisWorkFlow {
    @Resource(name = "attendanceService")
    private AttendanceService attendanceService;
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        int fineMoneyBasicOfDelay = attendanceService.getFineMoneyBasicOfDelay();
        int fineMoneyBasicOfLeaveEarly = attendanceService.getFineMoneyBasicOfLeaveEarly();
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Attendance attendance = analysisResult.getAttendance();
                    if (468 == attendance.getEmployee().getId()) {
                        System.out.println();
                    }
                    String workDateFormat = attendance.getWorkDateFormat();
                    String amTime = attendance.getAmTime();
                    Date amDate = StringUtils.isBlank(amTime) ? null : TimeUtils.parseDate(workDateFormat + " " + amTime + ":00");
                    String pmTime = attendance.getPmTime();
                    Date pmDate = StringUtils.isBlank(pmTime) ? null : TimeUtils.parseDate(workDateFormat + " " + pmTime + ":00");
                    if (amDate == null && pmDate == null && !analysisResult.isLeaveDay()) {
                        analysisResult.setWorkBad(true);
                    }
                    if (!analysisResult.isLeaveDay()) {
                        Date amNeedFitTime = analysisResult.getWorkFit();
                        Date pmNeedFitTime = analysisResult.getLeaveFit();
                        String remark = "";
                        // 如果迟到
                        if (amDate != null && amNeedFitTime != null && amDate.after(amNeedFitTime)) {
                            int delaySeconds = employeeAnalysisResult.getDelaySeconds();
                            long minutes = TimeUtils.getMinutes(amDate, amNeedFitTime);
                            analysisResult.setWorkDelayMinutes((int) minutes);
                            // 十五分钟内
                            if (minutes <= 15 && delaySeconds < attendanceService.getDelayLimit()) {
                                analysisResult.setImpunityWorkDelay(true);
                            } else {
                                // 如果超过15分钟
                                int amMoney = employeeAnalysisResult.getDelayMoney();
                                if (minutes <= 30) {
                                    amMoney += fineMoneyBasicOfDelay;
                                    remark += "迟到扣除" + amMoney + "元工资;";
                                } else if (minutes > 30 && minutes <= 60) {
                                    remark +=  "迟到扣除1h工资;";
                                } else if (minutes > 60 && minutes <= 180) {
                                    remark += "迟到扣除3h工资;";
                                } else if (minutes > 180) {
                                    remark += "迟到扣除1天工资;";
                                }
                                analysisResult.setLightFineWorkDelay(amMoney);
                                employeeAnalysisResult.setDelayMoney(amMoney);
                            }
                            employeeAnalysisResult.setDelaySeconds(delaySeconds + 1);
                        }
                        // 如果早退
                        if (pmDate != null && pmNeedFitTime != null && pmDate.before(pmNeedFitTime)) {
                            if (attendance.getEmployee().getId() == 562) {
                                System.out.println();
                            }
                            int goQuickSeconds = employeeAnalysisResult.getLeaveEarlySeconds();
                            if (goQuickSeconds < attendanceService.getLeaveEarlyLimit()) {
                                analysisResult.setImpunityLeaveEarly(true);
                                remark += "下班补卡;";
                            } else {
                                int pmMoney = employeeAnalysisResult.getLeaveEarlyMoney();
                                pmMoney += fineMoneyBasicOfLeaveEarly;
                                analysisResult.setFineLeaveEarly(pmMoney);
                                employeeAnalysisResult.setLeaveEarlyMoney(pmMoney);
                                remark += "早退扣除" + pmMoney + "元工资;";
                            }
                            long minutes = TimeUtils.getMinutes(pmNeedFitTime, pmDate);
                            analysisResult.setLeaveEarlyMinutes((int) minutes);
                            employeeAnalysisResult.setLeaveEarlySeconds(goQuickSeconds + 1);
                        }
                        analysisResult.setRemark(remark);
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
