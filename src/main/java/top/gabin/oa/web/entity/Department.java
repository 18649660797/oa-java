package top.gabin.oa.web.entity;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/13
 */
public interface Department {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    List<Employee> getEmployeeList();

    void setEmployeeList(List<Employee> employeeList);
}
