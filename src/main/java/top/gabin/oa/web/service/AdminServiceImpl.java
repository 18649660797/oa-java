/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminServiceImpl.
 */
package top.gabin.oa.web.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.gabin.oa.web.dao.AdminDao;
import top.gabin.oa.web.dao.CRUDDao;
import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.entity.Admin;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.entity.PermissionImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.utils.Md5Utils;

import javax.annotation.Resource;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/10
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService {
    @Resource(name = "crudDao")
    private CRUDDao crudDao;
    @Resource(name = "adminDao")
    private AdminDao adminDao;
    @Resource
    private CriteriaQueryService queryService;
    @Override
    public Admin findById(Long id) {
        return adminDao.findById(id);
    }

    @Transactional("transactionManager")
    @Override
    public Admin merge(AdminDTO adminDTO) {
        if (adminDTO != null) {
            Admin admin = new AdminImpl();
            if (adminDTO.getId() != null) {
                admin = findById(adminDTO.getId());
            }
            admin.setName(adminDTO.getName());
            if (StringUtils.isNotBlank(adminDTO.getPassword())) {
                try {
                    admin.setPassword(Md5Utils.encryptMD5(adminDTO.getPassword()));
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
            CriteriaCondition criteriaCondition = new CriteriaCondition();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", adminDTO.getGroups());
            criteriaCondition.setConditions(params);
            List<PermissionImpl> permissionList = queryService.query(PermissionImpl.class, criteriaCondition);
            admin.getPermissionList().clear();
            admin.getPermissionList().addAll(permissionList);
            adminDao.saveOrUpdate(admin);
        }
        return null;
    }

    @Transactional("transactionManager")
    @Override
    public void batchDelete(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("in_id", ids);
            CriteriaCondition condition = new CriteriaCondition();
            condition.setConditions(params);
            List<AdminImpl> adminList = queryService.query(AdminImpl.class, condition);
            if (adminList == null) {
                return;
            }
            for (Admin admin : adminList) {
                adminDao.delete(admin);
            }
        }
    }

}
