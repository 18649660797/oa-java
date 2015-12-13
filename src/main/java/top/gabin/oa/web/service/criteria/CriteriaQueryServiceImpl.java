/**
 * Copyright (c) 2015 云智盛世
 * Created with QueryServiceImpl.
 */
package top.gabin.oa.web.service.criteria;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import top.gabin.oa.web.dto.PageDTO;
import top.gabin.oa.web.entity.AdminImpl;
import top.gabin.oa.web.utils.RenderUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.*;

/**
 * Criteria Api查询实现类
 * @author linjiabin  on  15/11/13
 */
@Service("criteriaQueryService")
public class CriteriaQueryServiceImpl implements CriteriaQueryService {
    @PersistenceContext(name = "entityManagerFactory")
    private EntityManager em;


    @Override
    public <T> Long count(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        return selectCount(cb, root, query);
    }

    private <T> Long selectCount(CriteriaBuilder cb, Root<T> root, CriteriaQuery<T> query) {
        Expression ex = cb.countDistinct(root);
        query.select(ex);
        Long totalSize = (Long) em.createQuery(query).getSingleResult();
        return totalSize;
    }

    @Override
    public <T> PageDTO<T> queryPage(Class<T> entityClass, CriteriaCondition condition) {
        TypedQuery<T> tTypedQuery = typedQuery(entityClass, condition);
        Integer start = condition.getStart(), limit = condition.getLimit();
        if (limit == null) {
            limit = 15;
        }
        Integer page = (start / limit) + 1;;
        PageDTO<T> pageDTO = new PageDTO<T>(page, limit, 0, tTypedQuery.getResultList());
        Long count = count(entityClass, condition);
        pageDTO.setTotalSize(count);
        return pageDTO;
    }

    @Override
    public <T> Map<String, Object> queryPage(Class<T> entityClass, HttpServletRequest request, String pros) {
        CriteriaCondition criteriaCondition = CriteriaQueryUtils.parseCondition(request);
        PageDTO<AdminImpl> adminPageDTO = queryPage(AdminImpl.class, criteriaCondition);
        return RenderUtils.filterPageDataResult(adminPageDTO, pros);
    }

    @Override
    public <T> List<T> query(Class<T> entityClass, CriteriaCondition condition) {
        TypedQuery<T> typedQuery = typedQuery(entityClass, condition);
        return typedQuery.getResultList();
    }

    @Override
    public <T, Y> Y sum(Class<T> entityClass, Class<Y> targetClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Y count = selectSum(cb, root, query, condition.getTargetPath());
        return count;
    }

    private <Y> Y selectSum(CriteriaBuilder builder, Root root, CriteriaQuery query, String sumPath) {
        String[] names = StringUtils.splitByWholeSeparator(sumPath, ".");
        Expression ex = builder.sum(CriteriaQueryUtils.getPath(root, names));
        query.select(ex);
        Y totalSize = (Y) em.createQuery(query).getSingleResult();
        return totalSize;
    }

    private <T> TypedQuery<T> typedQuery(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(CriteriaQueryUtils.buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        query.select(root);
        List<Order> orders = CriteriaQueryUtils.parseOrderBy(cb, root, condition.getSort());
        if (orders.size() > 0) {
            query.orderBy(orders);
        }
        TypedQuery<T> typedQuery = em.createQuery(query);
        if (condition.getLimit() != null) {
            typedQuery.setMaxResults(condition.getLimit());
        }
        typedQuery.setFirstResult(condition.getStart());
        return typedQuery;
    }

    @Override
    public <T> T singleQuery(Class<T> entityClass, CriteriaCondition condition) {
        List<T> query = query(entityClass, condition);
        return query == null || query.isEmpty() ? null : query.get(0);
    }

}
