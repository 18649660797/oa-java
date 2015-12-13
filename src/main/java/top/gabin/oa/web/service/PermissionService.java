package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.SimpleTreeDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author linjiabin  on  15/12/13
 */
public interface PermissionService {
    List<SimpleTreeDTO> getPermissionTreeData(HttpServletRequest request);
}
