package top.gabin.oa.web.entity;

/**
 * Class description
 *
 * @author linjiabin  on  15/12/21
 */
public interface AttendanceRuleDetail {
    Long getId();

    void setId(Long id);

    String getRule();

    void setRule(String rule);

    Integer getLimit();

    void setLimit(Integer limit);
}
