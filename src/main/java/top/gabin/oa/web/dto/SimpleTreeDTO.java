/**
 * Copyright (c) 2015 云智盛世
 * Created with SimpleTreeDTO.
 */
package top.gabin.oa.web.dto;

/**
 * Class description
 *
 *
 *
 * @author linjiabin  on  15/8/1
 */
public class SimpleTreeDTO {
    // 显示标签
    private String text;
    // form值
    private String id;
    // 父节点的值
    private String pid;
    // 是否是叶子节点
    private boolean leaf;
    // cls自定义图标的样式，暂时没有用到
    private String cls;

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

}
