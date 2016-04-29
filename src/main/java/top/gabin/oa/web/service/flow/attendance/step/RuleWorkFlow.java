/**
 * Copyright (c) 2015 云智盛世
 * Created with RuleWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.service.AttendanceService;
import top.gabin.oa.web.service.BusinessService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 常规迟到、早退、旷工处理
 * @author linjiabin  on  15/12/17
 */
public class RuleWorkFlow extends AbstractAnalysisWorkFlow {
    @Resource(name = "attendanceService")
    private AttendanceService attendanceService;
    @Resource(name = "businessService")
    private BusinessService businessService;
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        int fineMoneyBasicOfDelay = attendanceService.getFineMoneyBasicOfDelay();
        int fineMoneyBasicOfLeaveEarly = attendanceService.getFineMoneyBasicOfLeaveEarly();
        AttendanceBasicRuleConfig attendanceBasicRule = businessService.getAttendanceBasicRule();
        Integer workFitOffset = attendanceBasicRule.getWorkFitOffset();
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Attendance attendance = analysisResult.getAttendance();
                    String workDateFormat = attendance.getWorkDateFormat();
                    String amTime = attendance.getAmTime();
                    Date amDate = StringUtils.isBlank(amTime) ? null : TimeUtils.parseDate(workDateFormat + " " + amTime + ":00");
                    String pmTime = attendance.getPmTime();
                    Date pmDate = StringUtils.isBlank(pmTime) ? null : TimeUtils.parseDate(workDateFormat + " " + pmTime + ":00");
                    if (amDate == null && pmDate == null && !analysisResult.isLeaveDay()) {
                        // 增加判断是否需要打卡的条件
                        if (analysisResult.isWorkNeedFit() && analysisResult.isLeaveNeedFit()) {
                            analysisResult.setWorkBad(true);
                        }
                    }
                    if (!analysisResult.isLeaveDay()) {
                        Date amNeedFitTime = analysisResult.getWorkFit();
                        if (workFitOffset != null && workFitOffset > 0) {
                            amNeedFitTime = DateUtils.addMinutes(amNeedFitTime, workFitOffset);
                        }
                        Date pmNeedFitTime = analysisResult.getLeaveFit();
                        String remark = "";
                        // 如果迟到
                        if (analysisResult.isWorkNeedFit() && amDate != null && amNeedFitTime != null && amDate.after(amNeedFitTime)) {
                            long minutes = TimeUtils.getMinutes(amDate, amNeedFitTime) + workFitOffset;
                            analysisResult.setWorkDelayMinutes((int) minutes);
                            int amMoney = employeeAnalysisResult.getDelayMoney();
                            if (minutes > 10 && minutes <= 30) {
                                remark +=  "迟到扣除0.5h工资;";
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
                        if (!analysisResult.isWorkNeedFit()) {
                            remark += "今天上班免打卡";
                        }
                        // 如果早退
                        if (analysisResult.isLeaveNeedFit() && ((amDate != null && pmDate == null) || (pmDate != null && pmNeedFitTime != null && pmDate.before(pmNeedFitTime)))) {
                            if (attendance.getEmployee().getId().equals(2680L)) {
                                System.out.println(1);
                            }
                            long minutes = 0;
                            if (pmDate != null && pmNeedFitTime != null) {
                                minutes = TimeUtils.getMinutes(pmNeedFitTime, pmDate);
                                analysisResult.setLeaveEarlyMinutes((int) minutes);
                            }
                            if (pmDate == null) {
                                minutes = 75 * 6;
                                analysisResult.setLeaveEarlyMinutes((int) minutes);
                            }
                            int goQuickSeconds = employeeAnalysisResult.getLeaveEarlySeconds();
                            if (goQuickSeconds < attendanceService.getLeaveEarlyLimit()) {
                                analysisResult.setImpunityLeaveEarly(true);
                                employeeAnalysisResult.setLeaveEarlySeconds(goQuickSeconds + 1);
                                remark += "下班补卡;";
                            } else if (minutes > 0) {
                                int pmMoney = employeeAnalysisResult.getLeaveEarlyMoney();
                                pmMoney += fineMoneyBasicOfLeaveEarly;
                                analysisResult.setFineLeaveEarly(pmMoney);
                                employeeAnalysisResult.setLeaveEarlyMoney(pmMoney);
                                int hour = (int)Math.ceil(minutes * 2 / 60d);
//                                remark += "早退乐捐" + pmMoney + "元;";
                                remark += "早退" + minutes + "分钟,扣除工资:" + hour +"小时工资";
                            }
                        }
                        if (!analysisResult.isLeaveNeedFit()) {
                            remark += "今天下班免打卡";
                        }
                        analysisResult.setRemark(remark);
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
