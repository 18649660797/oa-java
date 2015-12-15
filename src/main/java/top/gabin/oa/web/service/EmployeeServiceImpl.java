/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.dao.EmployeeDao;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.EmployeeDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {
    @Resource(name = "employeeDao")
    private EmployeeDao employeeDao;
    @Resource(name = "departmentService")
    private DepartmentService departmentService;
    @Resource
    private CriteriaQueryService queryService;

    @Transactional("transactionManager")
    @Override
    public boolean importEmployee(List<AttendanceImportDTO> attendanceImportDTOList) {
        Map<String, Integer> cache = new HashMap<String, Integer>();
        Map<String, Department> cacheDepartment = new HashMap<String, Department>();
        List<Employee> employees = new ArrayList<Employee>();
        for (AttendanceImportDTO dto : attendanceImportDTOList) {
            String realName = dto.getRealName();
            if (StringUtils.isBlank(realName)) {
                continue;
            }
            String departmentName = dto.getDepartment();
            if (!cache.containsKey(realName)) {
                Employee employee = new EmployeeImpl();
                employee.setName(realName);
                employee.setAttendanceCN(dto.getAttendance());
                Department department = null;
                if (cacheDepartment.containsKey(departmentName)) {
                    department = cacheDepartment.get(departmentName);
                    employee.setDepartment(department);
                } else {
                    department = departmentService.findByName(StringUtils.trim(departmentName));
                    if (department == null) {
                        department = new DepartmentImpl();
                        department.setName(departmentName);
                        departmentService.persist(department);
                    }
                    employee.setDepartment(department);
                    cacheDepartment.put(departmentName, department);
                }
                employees.add(employee);
                cache.put(realName, 1);
            }
        }
        for (Employee employee : employees) {
            employeeDao.saveOrUpdate(employee);
        }
        return true;
    }

    @Transactional("transactionManager")
    @Override
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<EmployeeImpl> employeeList = queryService.query(EmployeeImpl.class, condition);
            if (employeeList == null) {
                return;
            }
            for (Employee employee : employeeList) {
                employeeDao.delete(employee);
            }
        }
    }

    @Transactional("transactionManager")
    @Override
    public void batchInsert(List<Employee> employeeList) {
        employeeDao.batchInsert(employeeList);
    }

    @Override
    public Employee findById(Long id) {
        return employeeDao.findById(id);
    }

    @Override
    public Employee findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eq_name", name);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        return queryService.singleQuery(EmployeeImpl.class, criteriaCondition);
    }

    @Transactional("transactionManager")
    @Override
    public void merge(EmployeeDTO employeeDTO) {
        if (employeeDTO != null) {
            Employee employee = new EmployeeImpl();
            if (employeeDTO.getId() != null) {
                employee = employeeDao.findById(employeeDTO.getId());
            }
            if (employeeDTO.getName() != null) {
                employee.setName(employeeDTO.getName());
            }
            if (StringUtils.isNotBlank(employeeDTO.getAttendanceCN())) {
                employee.setAttendanceCN(employeeDTO.getAttendanceCN());
            }
            Long department = employeeDTO.getDepartment();
            if (department != null) {
                Department department1 = departmentService.findById(department);
                if (department1 != null) {
                    employee.setDepartment(department1);
                }
            }
            employeeDao.saveOrUpdate(employee);
        }
    }

    @Transactional("transactionManager")
    @Override
    public void merge(Employee employee) {
        employeeDao.saveOrUpdate(employee);
    }

    @Transactional("transactionManager")
    @Override
    public void persist(Employee employee) {
        employeeDao.persist(employee);
    }

    @Override
    public Employee findByAttendanceCN(String attendanceCN) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eq_attendanceCN", attendanceCN);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        return queryService.singleQuery(EmployeeImpl.class, criteriaCondition);
    }

}
