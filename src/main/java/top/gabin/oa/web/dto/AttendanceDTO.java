/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceDTO.
 */
package top.gabin.oa.web.dto;

/**
 * @author linjiabin  on  15/12/15
 */
public class AttendanceDTO {
    private Long id;
    private String workDate;
    private String amTime;
    private String pmTime;
    private Integer status;
    private Long employeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getAmTime() {
        return amTime;
    }

    public void setAmTime(String amTime) {
        this.amTime = amTime;
    }

    public String getPmTime() {
        return pmTime;
    }

    public void setPmTime(String pmTime) {
        this.pmTime = pmTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
