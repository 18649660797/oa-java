package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.entity.Admin;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/11
 */
public interface AdminService {
    Admin saveOrUpdate(Admin admin);
    Admin findById(Long id);
    Admin merge(AdminDTO adminDTO);
}
