/**
 * Copyright (c) 2015 云智盛世
 * Created with LeaveDTO.
 */
package top.gabin.oa.web.dto;

import java.util.Date;

/**
 * @author linjiabin  on  15/12/14
 */
public class LeaveDTO {
    private Long id;
    private Integer type;
    private Long employeeId;
    private Date beginDate;
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
