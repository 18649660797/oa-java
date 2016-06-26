/**
 * Copyright (c) 2016 云智盛世
 * Created with EmployeeServieTest.
 */
package top.gabin.oa.web.service;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.gabin.oa.BaseTest;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

import javax.annotation.Resource;

/**
 * Class description
 *
 * @author linjiabin on  16/6/23
 */
public class TestEmployeeService extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(TestEmployeeService.class);
    @Resource
    private EmployeeService employeeService;
    @Resource
    private DepartmentService departmentService;
    @Test
    public void testMerge1() {
        Department department = new DepartmentImpl();
        department.setName("技术部");
        department = departmentService.merge(department);
        EmployeeImpl employee = new EmployeeImpl();
        employee.setAttendanceCN("A33");
        employee.setName("林嘉斌");
        employee.setDepartment(department);
        employeeService.merge(employee);
        Employee a33 = employeeService.findByAttendanceCN("A33");
        logger.info(a33.getName());
    }
}
