package top.gabin.oa.web.service.criteria.simple;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Class description
 *
 * @author linjiabin  on  16/1/7
 */
public interface QueryCondition {
    List<Predicate> buildWhere(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery, From from);
}
