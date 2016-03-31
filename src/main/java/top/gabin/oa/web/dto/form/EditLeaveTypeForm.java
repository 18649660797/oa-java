/**
 * Copyright (c) 2016 云智盛世
 * Created with EditLeaveTypeForm.
 */
package top.gabin.oa.web.dto.form;

/**
 *
 * @author linjiabin  on  16/3/31
 */
public class EditLeaveTypeForm {
    private Long id;
    // 名称
    private String label;
    // 类型
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
