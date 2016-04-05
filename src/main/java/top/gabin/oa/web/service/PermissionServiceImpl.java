/**
 * Copyright (c) 2015 云智盛世
 * Created with PermissionServiceImpl.
 */
package top.gabin.oa.web.service;

import org.springframework.stereotype.Service;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.dto.SimpleTreeDTO;
import top.gabin.oa.web.entity.Permission;
import top.gabin.oa.web.entity.PermissionImpl;
import top.gabin.oa.web.service.criteria.CriteriaCondition;
import top.gabin.oa.web.service.criteria.CriteriaQueryService;
import top.gabin.oa.web.service.criteria.CriteriaQueryUtils;
import top.gabin.oa.web.service.criteria.simple.QueryService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author linjiabin  on  15/12/13
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Resource(name = "criteriaQueryService")
    private CriteriaQueryService criteriaQueryService;
    @Resource(name = "queryService")
    private QueryService queryService;

    @Override
    public List<SimpleTreeDTO> getPermissionTreeData(HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<PermissionImpl> permissionPageDTO = criteriaQueryService.queryPage(PermissionImpl.class, criteriaCondition);
        List<SimpleTreeDTO> list = new ArrayList<SimpleTreeDTO>();
        for (Permission permission : permissionPageDTO.getContent()) {
            SimpleTreeDTO dto = new SimpleTreeDTO();
            dto.setId(permission.getId() + "");
            dto.setPid(permission.getParent() == null ? "0" : permission.getParent().getId() + "");
            dto.setText(permission.getLabel());
            dto.setLeaf(permission.getChildrenList().isEmpty());
            list.add(dto);
        }
        return list;
    }

    @Override
    public List<Permission> findAll() {
        return queryService.query(Permission.class, PermissionImpl.class, null);
    }

    @Override
    public Permission findHelpPermissionTopById() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("eq_id", "4000");
        CriteriaCondition criteriaCondition = new CriteriaCondition(params);
        PermissionImpl permission = criteriaQueryService.singleQuery(PermissionImpl.class, criteriaCondition);
        return permission;
    }

    @Override
    public List<Permission> getChildren(Permission permission) {
        List<Permission> permissionList = new ArrayList<>();
        List<Permission> childrenList = permission.getChildrenList();
        permissionList.add(permission);
        if (childrenList.isEmpty()) {
            return permissionList;
        } else {
            for (Permission p : childrenList) {
                permissionList.addAll(getChildren(p));
            }
        }
        return permissionList;
    }

}
