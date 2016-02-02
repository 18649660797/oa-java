/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.dto.attendance.LeaveResult;
import top.gabin.oa.web.dto.business.AttendanceBasicRuleConfig;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.service.BusinessService;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 请假处理
 * @author linjiabin  on  15/12/17
 */
public class LeaveWorkFlow extends AbstractAnalysisWorkFlow {
    @Resource(name = "businessService")
    private BusinessService businessService;
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        AttendanceBasicRuleConfig attendanceBasicRule = businessService.getAttendanceBasicRule();
        String workFit = attendanceBasicRule.getWorkFit();
        String leaveFit = attendanceBasicRule.getLeaveFit();
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Attendance attendance = analysisResult.getAttendance();
                    analysisResult.setAttendance(attendance);
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    // 上午应打卡时间
                    Date amNeedFit = analysisResult.getWorkFit() == null ? TimeUtils.parseDate(workDateFormat + " " + workFit) : analysisResult.getWorkFit();
                    Date pmNeedFit = analysisResult.getLeaveFit() == null ? TimeUtils.parseDate(workDateFormat + " " + leaveFit) : analysisResult.getLeaveFit();
                    // 获取当天的请假记录
                    List<LeaveResult> leaveResultList = analysisResult.getLeaveList();
                    if (leaveResultList != null && !leaveResultList.isEmpty()) {
                        Collections.sort(leaveResultList, new Comparator<LeaveResult>() {
                            @Override
                            public int compare(LeaveResult o1, LeaveResult o2) {
                                return (int) (o2.getLeave().getBeginDate().getTime() - o1.getLeave().getBeginDate().getTime());
                            }
                        });
                        Date tmpBeginDate = amNeedFit;
                        Date tmpEndDate = pmNeedFit;
                        for (LeaveResult leaveResult : leaveResultList) {
                            Leave leave = leaveResult.getLeave();
                            Date beginDate = leave.getBeginDate();
                            Date endDate = leave.getEndDate();
                            long delayTimes = 0;
                            if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate) && TimeUtils.afterOrEqual(endDate, tmpEndDate)) {
                                delayTimes = 450l - analysisResult.getLeaveMinutes();
                                analysisResult.setIsLeaveDay(true);
                            } else if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate)) { // 请假开始时间在上午需要打卡的时刻或之前
                                // 请假结束时间在上午需要打卡之后
                                if (endDate.after(tmpBeginDate)) {
                                    // 如果请假结束时间在下午需要打卡之前
                                    if (endDate.before(tmpEndDate)) {
                                        tmpBeginDate = endDate;
                                    }
                                }
                                delayTimes = TimeUtils.getMinutes(endDate, TimeUtils.parseDate(workDateFormat + " " + workFit));
                            } else if (TimeUtils.afterOrEqual(endDate, tmpEndDate)) { // 请假结束时间在下午需要打卡的时刻或之后
                                delayTimes = TimeUtils.getMinutes(tmpEndDate, beginDate);
                                // 把需要打卡的时间修改为请假的结束时间
                                if (beginDate.before(tmpEndDate) && beginDate.after(tmpBeginDate)) {
                                    tmpEndDate = beginDate;
                                }
                            } else {
                                delayTimes = TimeUtils.getMinutes(endDate, beginDate);
                            }
                            String restBegin = attendanceBasicRule.getRestBegin();
                            String restEnd = attendanceBasicRule.getRestEnd();
                            if (tmpBeginDate.getTime() == TimeUtils.parseDate(workDateFormat + " " + restBegin).getTime()) {
                                tmpBeginDate = TimeUtils.parseDate(workDateFormat + " " + restEnd);
                            }
                            analysisResult.setWorkFit(tmpBeginDate);
                            if (tmpEndDate.getTime() == TimeUtils.parseDate(workDateFormat + " " + restEnd).getTime()) {
                                tmpEndDate = TimeUtils.parseDate(workDateFormat + " " + restBegin);
                            }
                            analysisResult.setLeaveFit(tmpEndDate);
                            if (!analysisResult.isLeaveDay()) {
                                if (TimeUtils.beforeOrEqual(beginDate, TimeUtils.parseDate(workDateFormat + " " + restBegin)) && TimeUtils.afterOrEqual(endDate, TimeUtils.parseDate(workDateFormat + " " + restEnd))) {
                                    delayTimes = delayTimes - 90;
                                }
                            }
                            leaveResult.setLeaveMinutes((int) delayTimes);
                            analysisResult.setLeaveMinutes((int) delayTimes + analysisResult.getLeaveMinutes());
                        }
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
