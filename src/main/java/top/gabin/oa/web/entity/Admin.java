package top.gabin.oa.web.entity;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/10
 */
public interface Admin {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getPassword();

    void setPassword(String password);

    List<Permission> getPermissionList();

    void setPermissionList(List<Permission> permissionList);

}
