/**
 * Copyright (c) 2016 云智盛世
 * Created with QueryServiceImpl.
 */
package top.gabin.oa.web.service.criteria.simple;

import org.springframework.stereotype.Service;
import top.gabin.oa.web.dto.PageDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

/**
 *
 * @author linjiabin  on  16/1/7
 */
@Service("queryService")
public class QueryServiceImpl implements QueryService {
    @PersistenceContext(name = "entityManagerFactory")
    private EntityManager em;

    @Override
    public <S, B extends  S> S querySingle(Class<S> sClass, Class<B> bClass, QueryCondition queryCondition) {
        CriteriaQuery<S> query = createQuery(sClass, bClass, queryCondition);
        TypedQuery<S> typedQuery = em.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Override
    public <S, B extends  S> List<S> query(Class<S> sClass, Class<B> bClass, QueryCondition queryCondition) {
        CriteriaQuery<S> query = createQuery(sClass, bClass, queryCondition);
        TypedQuery<S> typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
    }

    private <S, B extends S> CriteriaQuery<S> createQuery(Class<S> sClass, Class<B> bClass, QueryCondition queryCondition) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<S> query = criteriaBuilder.createQuery((sClass));
        Root<B> from = query.from(bClass);
        query.select(from);
        Predicate predicate = buildPredicate(criteriaBuilder, query, from, queryCondition);
        if (predicate != null) query.where(predicate);
        return query;
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery query, From from, QueryCondition queryCondition) {
        if (queryCondition != null) {
            List<Predicate> predicates = queryCondition.buildWhere(criteriaBuilder, query, from);
            Predicate predicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            return predicate;
        }
        return null;
    }

    @Override
    public <S, B extends S> PageDTO<S> pageQuery(Class<S> sClass, Class<B> bClass, PageQueryCondition queryCondition) {
        Long count = count(bClass, queryCondition);
        CriteriaQuery<S> query = createQuery(sClass, bClass, queryCondition);
        TypedQuery<S> typedQuery = em.createQuery(query);
        int start = queryCondition.getStart();
        typedQuery.setFirstResult(start);
        int limit = queryCondition.getLimit();
        typedQuery.setMaxResults(limit);
        List<S> resultList = typedQuery.getResultList();
        return new PageDTO<S>(start / limit + 1, limit, count, resultList);
    }

    @Override
    public <B> Long count(Class<B> bClass, QueryCondition queryCondition) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery((Long.class));
        Root<B> from = query.from(bClass);
        Expression<Long> longExpression = criteriaBuilder.countDistinct(from);
        query.select(longExpression);
        Predicate predicate = buildPredicate(criteriaBuilder, query, from, queryCondition);
        if (predicate != null) query.where(predicate);
        TypedQuery<Long> typedQuery = em.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Override
    public <T extends Number> T sum(Class<T> returnType, SumQueryCondition sumQueryCondition) {
        return null;
    }

}
