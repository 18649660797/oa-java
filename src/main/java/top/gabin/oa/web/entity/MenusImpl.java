/**
 * Copyright (c) 2016 云智盛世
 * Created with MenusImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjiabin  on  16/1/10
 */
@Entity
@Table(name = "edy_menus")
public class MenusImpl implements Menus {
    @Id
    @TableGenerator(name = "menus_sequences", table = "edy_sequences", pkColumnName = "sequence_name",
            valueColumnName = "sequence_next_hi_value", initialValue = 20, allocationSize = 50)
    @GeneratedValue(generator = "menus_sequences", strategy = GenerationType.TABLE)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;
    @Column(name = "level")
    private Integer level;
    @OneToOne(targetEntity = PermissionImpl.class)
    @JoinColumn(name = "permission_id")
    private Permission permission;
    @ManyToOne(targetEntity = MenusImpl.class)
    @JoinColumn(name = "parent_menus_id")
    private Menus parentMenus;
    @OneToMany(targetEntity = MenusImpl.class, mappedBy = "parentMenus")
    private List<Menus> childMenus = new ArrayList<Menus>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Permission getPermission() {
        return permission;
    }

    @Override
    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    @Override
    public Menus getParentMenus() {
        return parentMenus;
    }

    @Override
    public void setParentMenus(Menus parentMenus) {
        this.parentMenus = parentMenus;
    }

    @Override
    public List<Menus> getChildMenus() {
        return childMenus;
    }

    @Override
    public void setChildMenus(List<Menus> childMenus) {
        this.childMenus = childMenus;
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public void setLevel(Integer level) {
        this.level = level;
    }
}
