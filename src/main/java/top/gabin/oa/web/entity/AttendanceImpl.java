/**
 * Copyright (c) 2015 云智盛世
 * Created with AttendanceImpl.
 */
package top.gabin.oa.web.entity;

import org.apache.commons.lang3.time.DateUtils;
import top.gabin.oa.web.constant.AttendanceStatus;
import top.gabin.oa.web.dao.AttendanceDao;
import top.gabin.oa.web.dto.AttendanceDTO;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryServiceImpl;
import top.gabin.oa.web.utils.SpringBeanUtils;
import top.gabin.oa.web.utils.date.TimeUtils;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/15
 */
@Entity
@Table(name = "edy_attendance")
public class AttendanceImpl implements Attendance {
    @Id
    @TableGenerator(name = "attendance_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "attendance_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "work_date")
    private Date workDate;
    @Column(name = "am_time")
    private String amTime;
    @Column(name = "pm_time")
    private String pmTime;
    @Column(name = "status")
    private Integer status = 1;
    @ManyToOne(targetEntity = EmployeeImpl.class)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "work_date_format")
    private String workDateFormat;

    @Override
    public String getWorkDateFormat() {
        return workDateFormat;
    }

    @Override
    public void setWorkDateFormat(String workDateFormat) {
        this.workDateFormat = workDateFormat;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getWorkDate() {
        return workDate;
    }

    @Override
    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    @Override
    public String getAmTime() {
        return amTime;
    }

    @Override
    public void setAmTime(String amTime) {
        this.amTime = amTime;
    }

    @Override
    public String getPmTime() {
        return pmTime;
    }

    @Override
    public void setPmTime(String pmTime) {
        this.pmTime = pmTime;
    }

    @Override
    public AttendanceStatus getStatus() {
        return AttendanceStatus.instance(this.status);
    }

    @Override
    public void setStatus(AttendanceStatus status) {
        if (status != null) {
            this.status = status.getType();
        }
    }

    @Override
    public Employee getEmployee() {
        return employee;
    }

    @Override
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public String getYesterdayPm() {
        AttendanceDao attendanceDao = SpringBeanUtils.getBean(AttendanceDao.class);
        Attendance yesterday = attendanceDao.findYesterday(this);
        return yesterday == null ? null : yesterday.getPmTime();
    }

}
