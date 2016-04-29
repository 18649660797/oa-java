/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @author linjiabin  on  15/12/13
 */
@Entity
@Table(name = "edy_department")
public class DepartmentImpl extends BasicEntity implements Department {
    @Id
    @TableGenerator(name = "department_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "department_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "name")
    private String name;
    @OneToMany(targetEntity = EmployeeImpl.class, mappedBy = "department")
    private List<Employee> employeeList;

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
    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    @Override
    public void setEmployeeList(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
}
