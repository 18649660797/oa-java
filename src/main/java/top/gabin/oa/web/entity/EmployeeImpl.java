/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;

/**
 *
 * @author linjiabin  on  15/12/9
 */
@Entity
@Table(name = "edy_employee")
public class EmployeeImpl implements Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    @Column(name = "real_name")
    private String realName;
    @Column(name = "attendance_cn")
    private String attendanceCn;
    @Column(name = "department")
    private String department;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getRealName() {
        return realName;
    }

    @Override
    public void setRealName(String realName) {
        this.realName = realName;
    }

    @Override
    public String getAttendanceCn() {
        return attendanceCn;
    }

    @Override
    public void setAttendanceCn(String attendanceCn) {
        this.attendanceCn = attendanceCn;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }
}
