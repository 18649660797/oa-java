/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeDTO.
 */
package top.gabin.oa.web.dto;

/**
 * @author linjiabin  on  15/12/14
 */
public class EmployeeDTO {
    private Long id;
    private String name;
    private String attendanceCN;
    private Long department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttendanceCN() {
        return attendanceCN;
    }

    public void setAttendanceCN(String attendanceCN) {
        this.attendanceCN = attendanceCN;
    }

    public Long getDepartment() {
        return department;
    }

    public void setDepartment(Long department) {
        this.department = department;
    }
}
