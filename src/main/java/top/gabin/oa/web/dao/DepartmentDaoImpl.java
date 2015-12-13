/**
 * Copyright (c) 2015 云智盛世
 * Created with DepartmentDaoImpl.
 */
package top.gabin.oa.web.dao;

import org.springframework.stereotype.Repository;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;

/**
 * @author linjiabin  on  15/12/13
 */
@Repository("departmentDao")
public class DepartmentDaoImpl extends CommonBaseDaoImpl<Department, DepartmentImpl> implements DepartmentDao {

}
