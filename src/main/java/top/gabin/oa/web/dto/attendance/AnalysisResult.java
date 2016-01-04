/**
 * Copyright (c) 2015 云智盛世
 * Created with AnalysisResult.
 */
package top.gabin.oa.web.dto.attendance;

import top.gabin.oa.web.entity.Attendance;
import top.gabin.oa.web.entity.AttendanceRule;
import top.gabin.oa.web.entity.Leave;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 考勤分析后的结果类
 * @author linjiabin  on  15/12/17
 */
public class AnalysisResult {
    // 原始的考勤，用于工作流中获取数据
    private Attendance attendance;
    // 当天的请假
    private List<LeaveResult> leaveList = new ArrayList<LeaveResult>();
    // 上班需要打卡的时间
    private Date workFit;
    // 下班需要打卡的时间
    private Date leaveFit;
    // 昨天下午加班
    private boolean yesterdayWorkDelay;
    // 是否属于上班免乐捐
    private boolean impunityWorkDelay;
    // 上班乐捐
    private int lightFineWorkDelay;
    // 是否属于下班免乐捐
    private boolean impunityLeaveEarly;
    // 下班乐捐
    private int fineLeaveEarly;
    // 迟到罚款金额
    private int fineWorkDelayArray;
    // 迟到分钟
    private int workDelayMinutes;
    // 早退分钟
    private int leaveEarlyMinutes;
    // 是否工作日
    private boolean isWorkDay;
    // 是否请假一天，不需要打卡
    private boolean isLeaveDay;
    // 请假分钟数
    private int leaveMinutes;
    // 周几
    private String dayOfWeek;
    // 旷工
    private boolean workBad;
    // 备注
    private String remark;
    // 考勤高级规则
    private List<AttendanceRule> attendanceRuleList = new ArrayList<AttendanceRule>();

    public AnalysisResult() {
    }

    public List<AttendanceRule> getAttendanceRuleList() {
        return attendanceRuleList;
    }

    public void setAttendanceRuleList(List<AttendanceRule> attendanceRuleList) {
        this.attendanceRuleList = attendanceRuleList;
    }

    public AnalysisResult(Attendance attendance) {
        this.attendance = attendance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isWorkBad() {
        return workBad;
    }

    public void setWorkBad(boolean workBad) {
        this.workBad = workBad;
    }

    public boolean isYesterdayWorkDelay() {
        return yesterdayWorkDelay;
    }

    public void setYesterdayWorkDelay(boolean yesterdayWorkDelay) {
        this.yesterdayWorkDelay = yesterdayWorkDelay;
    }

    public int getLeaveMinutes() {
        return leaveMinutes;
    }

    public void setLeaveMinutes(int leaveMinutes) {
        this.leaveMinutes = leaveMinutes;
    }

    public Attendance getAttendance() {
        return attendance;
    }

    public void setAttendance(Attendance attendance) {
        this.attendance = attendance;
    }

    public List<LeaveResult> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<LeaveResult> leaveList) {
        this.leaveList = leaveList;
    }

    public Date getWorkFit() {
        return workFit;
    }

    public void setWorkFit(Date workFit) {
        this.workFit = workFit;
    }

    public Date getLeaveFit() {
        return leaveFit;
    }

    public void setLeaveFit(Date leaveFit) {
        if (attendance.getEmployee().getId() == 552 && attendance.getWorkDateFormat().equals("2015-11-09")) {
            System.out.println();
        }
        this.leaveFit = leaveFit;
    }

    public boolean isImpunityWorkDelay() {
        return impunityWorkDelay;
    }

    public void setImpunityWorkDelay(boolean impunityWorkDelay) {
        this.impunityWorkDelay = impunityWorkDelay;
    }

    public int getLightFineWorkDelay() {
        return lightFineWorkDelay;
    }

    public void setLightFineWorkDelay(int lightFineWorkDelay) {
        this.lightFineWorkDelay = lightFineWorkDelay;
    }

    public boolean isImpunityLeaveEarly() {
        return impunityLeaveEarly;
    }

    public void setImpunityLeaveEarly(boolean impunityLeaveEarly) {
        this.impunityLeaveEarly = impunityLeaveEarly;
    }

    public int getFineLeaveEarly() {
        return fineLeaveEarly;
    }

    public void setFineLeaveEarly(int fineLeaveEarly) {
        this.fineLeaveEarly = fineLeaveEarly;
    }

    public int getFineWorkDelayArray() {
        return fineWorkDelayArray;
    }

    public void setFineWorkDelayArray(int fineWorkDelayArray) {
        this.fineWorkDelayArray = fineWorkDelayArray;
    }

    public int getWorkDelayMinutes() {
        return workDelayMinutes;
    }

    public void setWorkDelayMinutes(int workDelayMinutes) {
        this.workDelayMinutes = workDelayMinutes;
    }

    public int getLeaveEarlyMinutes() {
        return leaveEarlyMinutes;
    }

    public void setLeaveEarlyMinutes(int leaveEarlyMinutes) {
        this.leaveEarlyMinutes = leaveEarlyMinutes;
    }

    public boolean isWorkDay() {
        return isWorkDay;
    }

    public void setIsWorkDay(boolean isWorkDay) {
        this.isWorkDay = isWorkDay;
    }

    public boolean isLeaveDay() {
        return isLeaveDay;
    }

    public void setIsLeaveDay(boolean isLeaveDay) {
        this.isLeaveDay = isLeaveDay;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void add(LeaveResult leave) {
        leaveList.add(leave);
    }

}
