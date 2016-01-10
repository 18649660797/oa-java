package top.gabin.oa.web.entity;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  16/1/10
 */
public interface Menus {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getUrl();

    void setUrl(String url);

    Permission getPermission();

    void setPermission(Permission permission);

    Menus getParentMenus();

    void setParentMenus(Menus parentMenus);

    List<Menus> getChildMenus();

    void setChildMenus(List<Menus> childMenus);

    Integer getLevel();

    void setLevel(Integer level);
}
