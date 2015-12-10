/**
 * Copyright (c) 2015 云智盛世
 * Created with AdminDTO.
 */
package top.gabin.oa.web.dto;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/12/10
 */
public class AdminDTO {
    private Long id;
    private String name;
    private String password;
    // 权限id集
    private String groups;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

}
