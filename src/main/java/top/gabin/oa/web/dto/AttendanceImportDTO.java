/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceImportDTO.
 */
package top.gabin.oa.web.dto;

import top.gabin.oa.web.utils.excel.annotation.ExcelField;

/**
 * @author linjiabin  on  15/12/14
 */
public class AttendanceImportDTO {
    @ExcelField(value = "attendance", title = "工号")
    private String attendance;
    @ExcelField(value = "realName", title = "姓名")
    private String realName;
    @ExcelField(value = "cn", title = "登记号码")
    private String cn;
    @ExcelField(value = "department", title = "部门")
    private String department;
    @ExcelField(value = "workData", title = "日期")
    private String workData;
    @ExcelField(value = "planNO", title = "班次")
    private String planNO;
    @ExcelField(value = "amTime1", title = "上班1")
    private String amTime1;
    @ExcelField(value = "pmTime1", title = "下班1")
    private String pmTime1;
    @ExcelField(value = "amTime2", title = "上班2")
    private String amTime2;
    @ExcelField(value = "pmTime2", title = "下班2")
    private String pmTime2;
    @ExcelField(value = "amTime3", title = "上班3")
    private String amTime3;
    @ExcelField(value = "pmTime3", title = "下班3")
    private String pmTime3;

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getWorkData() {
        return workData;
    }

    public void setWorkData(String workData) {
        this.workData = workData;
    }

    public String getPlanNO() {
        return planNO;
    }

    public void setPlanNO(String planNO) {
        this.planNO = planNO;
    }

    public String getAmTime1() {
        return amTime1;
    }

    public void setAmTime1(String amTime1) {
        this.amTime1 = amTime1;
    }

    public String getPmTime1() {
        return pmTime1;
    }

    public void setPmTime1(String pmTime1) {
        this.pmTime1 = pmTime1;
    }

    public String getAmTime2() {
        return amTime2;
    }

    public void setAmTime2(String amTime2) {
        this.amTime2 = amTime2;
    }

    public String getPmTime2() {
        return pmTime2;
    }

    public void setPmTime2(String pmTime2) {
        this.pmTime2 = pmTime2;
    }

    public String getAmTime3() {
        return amTime3;
    }

    public void setAmTime3(String amTime3) {
        this.amTime3 = amTime3;
    }

    public String getPmTime3() {
        return pmTime3;
    }

    public void setPmTime3(String pmTime3) {
        this.pmTime3 = pmTime3;
    }
}
