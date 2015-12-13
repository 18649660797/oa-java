/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

/**
 * @author linjiabin  on  15/12/14
 */
@Repository("employeeDao")
public class EmployeeDaoImpl extends CommonBaseDaoImpl<Employee, EmployeeImpl> implements EmployeeDao {
}
