package top.gabin.oa.web.entity;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/13
 */
public interface Employee {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getAttendanceCN();

    void setAttendanceCN(String attendanceCN);

    Department getDepartment();

    void setDepartment(Department department);
}
