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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjiabin  on  15/12/13
 */
@Service("permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private CriteriaQueryService queryService;

    @Override
    public List<SimpleTreeDTO> getPermissionTreeData(HttpServletRequest request) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<PermissionImpl> permissionPageDTO = queryService.queryPage(PermissionImpl.class, criteriaCondition);
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
}
