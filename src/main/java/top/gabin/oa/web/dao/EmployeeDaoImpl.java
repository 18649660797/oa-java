/**
 * Copyright (c) 2015 云智盛世
 * Created with EmployeeDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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

    @Override
    public List<Long> findIdByDepartmentId(Long departmentId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<EmployeeImpl> employeeRoot = criteriaQuery.from(EmployeeImpl.class);
        Path<Long> id = employeeRoot.get("id");
        criteriaQuery.select(id);
        Path<Long> departmentIdPath = employeeRoot.get("department").get("id");
        criteriaQuery.where(criteriaBuilder.equal(departmentIdPath, departmentId));
        TypedQuery<Long> typedQuery = em.createQuery(criteriaQuery);
        List<Long> resultList = typedQuery.getResultList();
        if (resultList == null) {
            resultList = new ArrayList<>();
        }
        return resultList;
    }

    @Override
    public Employee findByAttendanceCN(String attendanceCN) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Employee> criteriaQuery = criteriaBuilder.createQuery(Employee.class);
        Root<EmployeeImpl> employeeRoot = criteriaQuery.from(EmployeeImpl.class);
        criteriaQuery.select(employeeRoot);
        criteriaQuery.where(criteriaBuilder.equal(employeeRoot.get("attendanceCN"), attendanceCN));
        TypedQuery<Employee> employeeTypedQuery = em.createQuery(criteriaQuery);
        try {
            return employeeTypedQuery.getSingleResult();
        } catch (NoResultException e) {
        }
        return null;
    }

}
