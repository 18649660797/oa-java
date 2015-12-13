package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.EmployeeDTO;
import top.gabin.oa.web.entity.Employee;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/14
 */
public interface EmployeeService {
    boolean importEmployee(List<AttendanceImportDTO> attendanceImportDTOList);
    void batchDelete(String ids);
    void batchInsert(List<Employee> employeeList);
    Employee findById(Long id);
    void merge(EmployeeDTO employeeDTO);
    Employee findByAttendanceCN(String attendanceCN);
}
