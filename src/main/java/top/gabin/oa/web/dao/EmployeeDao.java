package top.gabin.oa.web.dao;

import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
public interface EmployeeDao extends CommonBaseDao<Employee, EmployeeImpl> {
    Map<String,Long> findAllNameMapId();
}
