/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author linjiabin  on  15/12/10
 */
@Entity
@Table(name = "edy_admin")
public class AdminImpl implements Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "password")
    private String password;
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = PermissionImpl.class)
    @JoinTable (name =  "edy_admin_permission" ,
            joinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"),
            inverseJoinColumns =  @JoinColumn (name =  "permission_id" , referencedColumnName = "id"))
    private List<Permission> permissionList = new ArrayList<Permission>();

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
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public List<Permission> getPermissionList() {
        return permissionList;
    }

    @Override
    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

}
