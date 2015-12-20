/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
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
 * Class description
 *
 *
 *
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
                    Date amNeedFit = TimeUtils.parseDate(workDateFormat + " " + workFit);
                    Date pmNeedFit = TimeUtils.parseDate(workDateFormat + " " + leaveFit);
                    // 获取当天的请假记录
                    List<Leave> leaveList = analysisResult.getLeaveList();
                    if (leaveList != null && !leaveList.isEmpty()) {
                        Collections.sort(leaveList, new Comparator<Leave>() {
                            @Override
                            public int compare(Leave o1, Leave o2) {
                                return (int) (o1.getBeginDate().getTime() - o2.getBeginDate().getTime());
                            }
                        });
                        // 获取请假时长
                        Date tmpBeginDate = amNeedFit;
                        Date tmpEndDate = pmNeedFit;
                        for (Leave leave : leaveList) {
                            Date beginDate = leave.getBeginDate();
                            Date endDate = leave.getEndDate();
                            long delayTimes = 0;
                            if (TimeUtils.beforeOrEqual(beginDate, tmpBeginDate) && TimeUtils.afterOrEqual(endDate, tmpEndDate)) {
                                analysisResult.setLeaveMinutes(450);
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
                                // 请假开始时间在上午需要打卡之前
                                if (beginDate.before(tmpEndDate)) {
                                    if (beginDate.after(tmpBeginDate)) {
                                        tmpEndDate = beginDate;
                                    }
                                }
                                delayTimes = TimeUtils.getMinutes(TimeUtils.parseDate(workDateFormat + " " + leaveFit), beginDate);
                            } else {
                                delayTimes = TimeUtils.getMinutes(endDate, beginDate);
                            }
                            if (attendance.getEmployee().getId() == 562) {
                                System.out.println();
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
                                analysisResult.setLeaveMinutes((int) delayTimes);
                            }
                        }
                    }
                }
            }
        }
        return departmentAnalysisResultList;
    }
}
