package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.SimpleTreeDTO;
import top.gabin.oa.web.entity.Permission;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author linjiabin  on  15/12/13
 */
public interface PermissionService {
    List<SimpleTreeDTO> getPermissionTreeData(HttpServletRequest request);
    List<Permission> findAll();
    Permission findHelpPermissionTopById();
    List<Permission> getChildren(Permission permission);
}
