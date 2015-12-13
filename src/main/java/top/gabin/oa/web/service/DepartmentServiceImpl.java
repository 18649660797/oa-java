/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentService.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.dao.DepartmentDao;
import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;

import javax.annotation.Resource;
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
    @Resource
    private CriteriaQueryService queryService;

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
            List<DepartmentImpl> departmentList = queryService.query(DepartmentImpl.class, condition);
            if (departmentList == null) {
                return;
            }
            for (Department department : departmentList) {
                departmentDao.delete(department);
            }
        }
    }

    @Override
    public Department findByName(String name) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eq_name", name);
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        return queryService.singleQuery(DepartmentImpl.class, criteriaCondition);
    }

    @Transactional("transactionManager")
    @Override
    public void batchSave(List<Department> departmentList) {
        if (departmentList != null) {
            for (Department department : departmentList) {
                departmentDao.saveOrUpdate(department);
            }
        }
    }

}
