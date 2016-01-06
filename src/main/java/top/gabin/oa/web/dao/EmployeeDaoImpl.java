/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/14
 */
@Repository("employeeDao")
public class EmployeeDaoImpl extends CommonBaseDaoImpl<Employee, EmployeeImpl> implements EmployeeDao {
    @Override
    public Map<String, Long> findAllNameMapId() {
        List<Employee> resultList = findAll();
        Map<String, Long> map = new HashMap<String, Long>();
        if (!resultList.isEmpty()) {
            for (Employee employee : resultList) {
                map.put(employee.getName(), employee.getId());
            }
        }
        return map;
    }
}
