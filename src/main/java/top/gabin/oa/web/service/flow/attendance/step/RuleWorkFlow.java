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
                    Date amRestTime = TimeUtils.parseDate(workDateFormat + " " + attendanceBasicRule.getRestBegin());
                    Date pmRestTime = TimeUtils.parseDate(workDateFormat + " " + attendanceBasicRule.getRestEnd());
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
                        // 工作时长(理论)
                        long workTimes = TimeUtils.getMinutes(pmNeedFitTime, amNeedFitTime) + workFitOffset - TimeUtils.getMinutes(pmRestTime, amRestTime);
                        String remark = "";
                        if (!analysisResult.isWorkNeedFit()) {
                            remark += "今天上班免打卡";
                        } else if (amDate != null && amNeedFitTime != null && amDate.after(amNeedFitTime)) {
                            // 如果迟到
                            long minutes = TimeUtils.getMinutes(amDate, amNeedFitTime) + workFitOffset;
                            if (amDate.after(pmNeedFitTime)) {
                                minutes = workTimes;
                            } else if (amDate.after(pmRestTime)) {
                                minutes = TimeUtils.getMinutes(amDate, amNeedFitTime) + workFitOffset - TimeUtils.getMinutes(pmRestTime, amRestTime);
                            }
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
                        if (!analysisResult.isLeaveNeedFit()) {
                            remark += "今天下班免打卡";
                        } else if (((amDate != null && pmDate == null) || (pmDate != null && pmNeedFitTime != null && pmDate.before(pmNeedFitTime)))) {
                            // 如果早退
                            long minutes = 0;
                            if (pmDate != null && pmNeedFitTime != null) {
                                minutes = TimeUtils.getMinutes(pmNeedFitTime, pmDate);
                                if (pmDate.before(amRestTime)) {
                                    minutes -= TimeUtils.getMinutes(pmRestTime, amRestTime);
                                } else if (pmDate.before(amNeedFitTime)) {
                                    minutes = workTimes;
                                }
                                analysisResult.setLeaveEarlyMinutes((int) minutes);
                            } else if (pmDate == null) {
                                minutes = workTimes;
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
                                remark += "早退" + minutes + "分钟,扣除工资:" + hour +"小时工资";
                            }
                        }
                        analysisResult.setRemark(remark);
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
