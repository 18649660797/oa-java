package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.DepartmentDTO;
import top.gabin.oa.web.entity.Department;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/13
 */
public interface DepartmentService {
    void merge(DepartmentDTO departmentDTO);
    Department findById(Long id);
    void batchDelete(String ids);
    Department findByName(String name);
    void batchSave(List<Department> departmentList);
}
