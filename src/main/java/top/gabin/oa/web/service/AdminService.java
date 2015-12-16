package top.gabin.oa.web.service;

import top.gabin.oa.web.dto.AdminDTO;
import top.gabin.oa.web.entity.Admin;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/11
 */
public interface AdminService {
    Admin findById(Long id);
    Admin findByName(String name);
    Admin merge(AdminDTO adminDTO);
    void batchDelete(String ids);

    /**
     * 重新设置密码
     * @param id
     * @param password
     */
    void reset(Long id, String password);
}
