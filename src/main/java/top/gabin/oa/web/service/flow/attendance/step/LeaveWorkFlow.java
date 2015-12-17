/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveWorkFlow.
 */
package top.gabin.oa.web.service.flow.attendance.step;

import top.gabin.oa.web.dto.attendance.AnalysisResult;
import top.gabin.oa.web.dto.attendance.DepartmentAnalysisResult;
import top.gabin.oa.web.dto.attendance.EmployeeAnalysisResult;
import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.Leave;
import top.gabin.oa.web.utils.date.TimeUtils;

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
    @Override
    public List<DepartmentAnalysisResult> analysis(List<DepartmentAnalysisResult> departmentAnalysisResultList) {
        for (DepartmentAnalysisResult departmentAnalysisResult : departmentAnalysisResultList) {
            for (EmployeeAnalysisResult employeeAnalysisResult : departmentAnalysisResult.getEmployeeAnalysisResultList()) {
                for (AnalysisResult analysisResult : employeeAnalysisResult.getAnalysisResultList()) {
                    Attendance attendance = analysisResult.getAttendance();
                    analysisResult.setAttendance(attendance);
                    Long id = attendance.getEmployee().getId();
                    String workDateFormat = TimeUtils.format(attendance.getWorkDate(), "yyyy-MM-dd");
                    // 上午应打卡时间
                    Date amNeedFit = TimeUtils.parseDate(workDateFormat + " 09:00:00");
                    Date pmNeedFit = TimeUtils.parseDate(workDateFormat + " 18:00:00");
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
                                delayTimes = TimeUtils.getMinutes(endDate, TimeUtils.parseDate(workDateFormat + " 09:00:00"));
                            } else if (TimeUtils.afterOrEqual(endDate, tmpEndDate)) { // 请假结束时间在下午需要打卡的时刻或之后
                                // 请假开始时间在上午需要打卡之前
                                if (beginDate.before(tmpEndDate)) {
                                    if (beginDate.after(tmpBeginDate)) {
                                        tmpEndDate = beginDate;
                                    }
                                }
                                delayTimes = TimeUtils.getMinutes(TimeUtils.parseDate(workDateFormat + " 18:00:00"), beginDate);
                            } else {
                                delayTimes = TimeUtils.getMinutes(endDate, beginDate);
                            }
                            if (tmpBeginDate.getTime() == TimeUtils.parseDate(workDateFormat + " 12:00:00").getTime()) {
                                tmpBeginDate = TimeUtils.parseDate(workDateFormat + " 13:30:00");
                            }
                            analysisResult.setWorkFit(tmpBeginDate);
                            if (tmpEndDate.getTime() == TimeUtils.parseDate(workDateFormat + " 13:30:00").getTime()) {
                                tmpEndDate = TimeUtils.parseDate(workDateFormat + " 12:00:00");
                            }
                            analysisResult.setLeaveFit(tmpEndDate);
                            if (!analysisResult.isLeaveDay()) {
                                if (TimeUtils.beforeOrEqual(beginDate, TimeUtils.parseDate(workDateFormat + " 12:00:00")) && TimeUtils.afterOrEqual(endDate, TimeUtils.parseDate(workDateFormat + " 13:30:00"))) {
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
