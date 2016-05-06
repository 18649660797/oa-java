/**
 * Copyright (c) 2015 云智盛世
 * Created with Leave.
 */
package top.gabin.oa.web.entity;

import top.gabin.oa.web.constant.LeaveType;

import javax.persistence.*;
import java.util.Date;

/**
 * @author linjiabin  on  15/12/14
 */
@Entity
@Table(name = "edy_leave")
public class LeaveImpl implements Leave {
    @Id
    @TableGenerator(name = "leave_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "leave_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "begin_date")
    private Date beginDate;
    @Column(name = "end_date")
    private Date endDate;
    @ManyToOne(targetEntity = EmployeeImpl.class)
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @Column(name = "type")
    private Integer type;
    @Column(name = "remark")
    private String remark;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
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
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getBeginDate() {
        return beginDate;
    }

    @Override
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
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
    public void setType(LeaveType leaveType) {
        if (leaveType != null) {
            this.type = leaveType.getType();
        }
    }

    @Override
    public LeaveType getType() {
        return LeaveType.instance(this.type);
    }

}
