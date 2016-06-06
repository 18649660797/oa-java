package top.gabin.oa.web.entity;

import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/10
 */
public interface Permission {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    String getLabel();

    void setLabel(String label);

    Permission getParent();

    void setParent(Permission parent);

    boolean isLeaf();

    List<Permission> getChildrenList();

    void setChildrenList(List<Permission> childrenList);

}
