/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeAttendanceDTO.
 */
package top.gabin.oa.web.dto;

import java.util.List;

/**
 * Class description
 * @author linjiabin  on  15/12/16
 */
public class EmployeeAttendanceDTO {
    // 十五分钟内迟到次数
    public static int delayLimit = 3;
    // 个人每月早退限制次数
    public static int goQuickLimit = 4;
    private List<AttendanceWorkFlowDTO> attendanceWorkFlowDTOList;
    private int delaySeconds = 0;
    private int delayMoneys = 0;
    private int goQuickSeconds = 0;
    private int goQuickMoneys = 0;

    public int getDelayMoneys() {
        return delayMoneys;
    }

    public void setDelayMoneys(int delayMoneys) {
        this.delayMoneys = delayMoneys;
    }

    public int getGoQuickMoneys() {
        return goQuickMoneys;
    }

    public void setGoQuickMoneys(int goQuickMoneys) {
        this.goQuickMoneys = goQuickMoneys;
    }

    public List<AttendanceWorkFlowDTO> getAttendanceWorkFlowDTOList() {
        return attendanceWorkFlowDTOList;
    }

    public void setAttendanceWorkFlowDTOList(List<AttendanceWorkFlowDTO> attendanceWorkFlowDTOList) {
        this.attendanceWorkFlowDTOList = attendanceWorkFlowDTOList;
    }

    public int getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(int delaySeconds) {
        this.delaySeconds = delaySeconds;
    }

    public int getGoQuickSeconds() {
        return goQuickSeconds;
    }

    public void setGoQuickSeconds(int goQuickSeconds) {
        this.goQuickSeconds = goQuickSeconds;
    }

}
