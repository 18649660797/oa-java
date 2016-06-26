/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentService.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.dao.DepartmentDao;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/13
 */
@Service("departmentService")
public class DepartmentServiceImpl implements DepartmentService {
    @Resource(name = "departmentDao")
    private DepartmentDao departmentDao;
    @Resource(name = "employeeService")
    private EmployeeService employeeService;
    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;

    @Transactional("transactionManager")
    @Override
    public void merge(DepartmentDTO departmentDTO) {
        if (departmentDTO != null) {
            Department department = new DepartmentImpl();
            if (departmentDTO.getId() != null) {
                department = departmentDao.findById(departmentDTO.getId());
            }
            if (departmentDTO.getName() != null) {
                department.setName(departmentDTO.getName());
            }
            departmentDao.saveOrUpdate(department);
        }

    }

    @Transactional("transactionManager")
    @Override
    public Department merge(Department department) {
        return departmentDao.saveOrUpdate(department);
    }

    @Override
    public void persist(Department department) {
        departmentDao.persist(department);
    }

    @Override
    public Department findById(Long id) {
        return departmentDao.findById(id);
    }

    @Transactional("transactionManager")
    @Override
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<DepartmentImpl> departmentList = criteriaQueryService.query(DepartmentImpl.class, condition);
            if (departmentList == null) {
                return;
            }
            for (Department department : departmentList) {
                List<Long> employeeIds = employeeService.findIdByDepartmentId(department.getId());
                employeeService.batchDelete(StringUtils.join(employeeIds, ","));
                departmentDao.delete(department);
            }
        }
    }

    @Override
    public Department findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eq_name", name);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        return criteriaQueryService.singleQuery(DepartmentImpl.class, criteriaCondition);
    }

    @Transactional("transactionManager")
    @Override
    public void importDepartment(List<AttendanceImportDTO> dataList) {
        Map<String, Integer> cache = new HashMap<String, Integer>();
        List<Department> departmentList = new ArrayList<Department>();
        for (AttendanceImportDTO dto : dataList) {
            String departmentName = dto.getDepartment();
            if (StringUtils.isBlank(departmentName) || findByName(departmentName) != null) {
                continue;
            }
            if (!cache.containsKey(departmentName)) {
                DepartmentImpl department = new DepartmentImpl();
                department.setName(departmentName);
                departmentList.add(department);
                cache.put(dto.getDepartment(), 1);
            }
        }
        if (departmentList != null) {
            for (Department department : departmentList) {
                departmentDao.saveOrUpdate(department);
            }
        }
    }

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }
}
