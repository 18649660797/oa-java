/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;

/**
 * @author linjiabin  on  15/12/13
 */
@Entity
@Table(name = "edy_employee")
public class EmployeeImpl implements Employee {
    @Id
    @TableGenerator(name = "employee_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "employee_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "attendance_cn")
    private String attendanceCN;
    @ManyToOne(targetEntity = DepartmentImpl.class)
    @JoinColumn(name = "department_id")
    private Department department;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAttendanceCN() {
        return attendanceCN;
    }

    @Override
    public void setAttendanceCN(String attendanceCN) {
        this.attendanceCN = attendanceCN;
    }

    @Override
    public Department getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(Department department) {
        this.department = department;
    }
}
