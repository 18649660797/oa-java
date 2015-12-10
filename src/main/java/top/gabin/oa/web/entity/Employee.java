package top.gabin.oa.web.entity;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/9
 */
public interface Employee {
    Long getId();

    void setId(Long id);

    String getRealName();

    void setRealName(String realName);

    String getAttendanceCn();

    void setAttendanceCn(String attendanceCn);

    String getDepartment();

    void setDepartment(String department);
}
