/**
 * Copyright (c) 2016 云智盛世
 * Created with MenusDTO.
 */
package top.gabin.oa.web.dto.menus;

import java.io.Serializable;
import java.util.List;

/**
 * @author linjiabin  on  16/1/10
 */
public class MenusDTO implements Serializable {
    private Long id;
    private String name;
    private String url;
    private List<MenusDTO> childMenus;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<MenusDTO> getChildMenus() {
        return childMenus;
    }

    public void setChildMenus(List<MenusDTO> childMenus) {
        this.childMenus = childMenus;
    }
}
