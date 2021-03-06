/**
 * Copyright (c) 2015 云智盛世
 * Created with CriteriaConditionPojo.
 */
package top.gabin.oa.web.service.criteria;

/**
 * 查询条件dto
 * @author linjiabin  on  15/11/13
 */
public class CriteriaConditionPojo {
    private String field;
    private Object value;
    private CriteriaOperation operation;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public CriteriaOperation getOperation() {
        return operation;
    }

    public void setOperation(CriteriaOperation operation) {
        this.operation = operation;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
