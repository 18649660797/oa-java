/**
 * Copyright (c) 2015 云智盛世
 * Created with PermissionImpl.
 */
package top.gabin.oa.web.entity;

import javax.persistence.*;

/**
 * @author linjiabin  on  15/12/10
 */
@Entity
@Table(name = "edy_permission")
public class PermissionImpl implements Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "label")
    private String label;
    @ManyToOne(targetEntity = PermissionImpl.class)
    @JoinColumn(name = "pid")
    private Permission parent;

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
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Permission getParent() {
        return parent;
    }

    @Override
    public void setParent(Permission parent) {
        this.parent = parent;
    }
}
