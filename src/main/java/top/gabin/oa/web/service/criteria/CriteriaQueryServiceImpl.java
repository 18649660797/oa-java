/**
 * Copyright (c) 2015 云智盛世
 * Created with QueryServiceImpl.
 */
package top.gabin.oa.web.service.criteria;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import top.gabin.oa.web.dto.PageDTO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
    private static final Set<String> PARSE_PATTERNS = new HashSet<String>();
    static {
        PARSE_PATTERNS.add("yyyy-MM-dd");
        PARSE_PATTERNS.add("yyyy-MM-dd HH:mm:ss.SSS");
        PARSE_PATTERNS.add("yyyy-MM-dd HH:mm:ss");
        PARSE_PATTERNS.add("yyyyMMddHHmmss");
    }

    @Override
    public <T> Long count(Class<T> entityClass, CriteriaCondition condition) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);
        query.distinct(condition.isDistinct());
        List<Predicate> predicates = null == condition.getCallBack() ? new ArrayList<Predicate>() : condition.getCallBack().callback(cb, query, root);
        predicates.addAll(buildPredicateList(root, cb, condition.getConditions()));
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
        predicates.addAll(buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        Y count = selectSum(cb, root, query, condition.getTargetPath());
        return count;
    }

    private <Y> Y selectSum(CriteriaBuilder builder, Root root, CriteriaQuery query, String sumPath) {
        String[] names = StringUtils.splitByWholeSeparator(sumPath, ".");
        Expression ex = builder.sum(getPath(root, names));
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
        predicates.addAll(buildPredicateList(root, cb, condition.getConditions()));
        query.where(cb.and(predicates.toArray(new Predicate[predicates.size()])));
        query.select(root);
        List<Order> orders = parseOrderBy(cb, root, condition.getSort());
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

    private List<Predicate> buildPredicateList(Root root, CriteriaBuilder cb, Map<String, Object> conditions) {
        List<Predicate> predicateList = new ArrayList<Predicate>();
        List<CriteriaConditionGroup> groups = buildConditionGroups(conditions);
        for (CriteriaConditionGroup group : groups) {
            List<Predicate> predicateList1 = buildPredicateList(root, cb, group);
            if (predicateList1 != null) {
                predicateList.addAll(predicateList1);
            }
        }
        return predicateList;
    }

    private List<Predicate> buildPredicateList(Root root, CriteriaBuilder criteriaBuilder, CriteriaConditionGroup criteriaConditionGroup) {
        if (criteriaConditionGroup == null) {
            return null;
        }
        List<Predicate> predicateList = new ArrayList<Predicate>();
        String join = criteriaConditionGroup.getJoin();
        From from = StringUtils.isNotBlank(join) ? root.join(join, JoinType.LEFT) : root;
        for (CriteriaConditionPojo conditionEntity : criteriaConditionGroup.getAndConditions()) {
            String fieldName = conditionEntity.getField();
            CriteriaOperation operation = conditionEntity.getOperation();
            Object fieldValue = conditionEntity.getValue();
            String[] names = StringUtils.splitByWholeSeparator(fieldName, ".");
            Path expression = getPath(from, names);
            Predicate predicate = buildPredicate(criteriaBuilder, expression, operation, fieldValue);
            predicateList.add(predicate);
        }
        if (CriteriaConditionGroup.GROUP_OPERATION_OR.equals(criteriaConditionGroup.getGroupOperation())) {
            List<Predicate> tmpList = new ArrayList<Predicate>();
            tmpList.add(criteriaBuilder.or(predicateList.toArray(new Predicate[predicateList.size()])));
            return tmpList;
        }
        return predicateList;
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Path expression, CriteriaOperation operation, Object fieldValue) {

        switch (operation) {
            case between:
                String[] values = StringUtils.splitByWholeSeparator((String)fieldValue, ",");
                if (Date.class.equals(expression.getJavaType())) {
                    Date startDate = parseDate(values[0]);
                    Date endDate = parseDate(values[1]);
                    return criteriaBuilder.between(expression, startDate, endDate);
                }
                return criteriaBuilder.between(expression, values[0], values[1]);
            case equals:
            case eq:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                return criteriaBuilder.equal(expression, fieldValue);
            case en:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.equal(expression, dataValue);
                }
                return criteriaBuilder.or(criteriaBuilder.equal(expression, fieldValue), criteriaBuilder.isNull(expression));
            case notEqual:
            case ne:
                if (Boolean.class.equals(expression.getJavaType())) {
                    Boolean dataValue = Boolean.parseBoolean((String) fieldValue);
                    return criteriaBuilder.notEqual(expression, dataValue);
                }
                if (Character.class.equals(expression.getJavaType())) {
                    Character dataValue = (fieldValue + "").charAt(0);
                    return criteriaBuilder.notEqual(expression, dataValue);
                }
                return criteriaBuilder.notEqual(expression, fieldValue);
            case greaterThan:
            case gt:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThan(expression, dataValue);
                }
                return criteriaBuilder.greaterThan(expression, (Comparable) fieldValue);
            case greaterThanEqualTo:
            case ge:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) fieldValue);
            case in:
                return expression.in(StringUtils.splitByWholeSeparator((String) fieldValue, ","));
            case notIn:
                return criteriaBuilder.not(expression.in(StringUtils.splitByWholeSeparator((String) fieldValue, ",")));
            case lessThan:
            case lt:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.lessThan(expression, dataValue);
                }
                return criteriaBuilder.lessThan(expression, (Comparable) fieldValue);
            case lessThanEqualTo:
            case le:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.lessThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) fieldValue);
            case like:
                return criteriaBuilder.like(expression, "%" + fieldValue + "%");
            case bw:
                return criteriaBuilder.like(expression, fieldValue + "%");
            case ew:
                return criteriaBuilder.like(expression, "%" + fieldValue);
            case isNull:
                return criteriaBuilder.isNull(expression);
            case isNotNull:
                return criteriaBuilder.isNotNull(expression);
            case isTrue:
                return criteriaBuilder.isTrue(expression);
            case isFalse:
                return criteriaBuilder.isFalse(expression);
            case gn:
                if (Date.class.equals(expression.getJavaType())) {
                    Date dataValue = parseDate((String) fieldValue);
                    return criteriaBuilder.greaterThanOrEqualTo(expression, dataValue);
                }
                return criteriaBuilder.or(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) fieldValue), criteriaBuilder.isNull(expression));
        }
        return null;
    }


    private List<CriteriaConditionGroup> buildConditionGroups(Map<String, Object> conditions) {
        List<CriteriaConditionGroup> groups = new ArrayList<CriteriaConditionGroup>();
        if (conditions == null) {
            return groups;
        }
        for (String key : conditions.keySet()) {
            Object value = conditions.get(key);
            if (value == null || StringUtils.isBlank(value.toString())) {
                continue;
            }
            groups.add(parsePojo(key, value.toString()));
        }
        return groups;
    }

    private CriteriaConditionGroup parsePojo(String key, String value) {
        CriteriaConditionGroup group = new CriteriaConditionGroup();
        if (key.indexOf("_join_") > -1) {
            String[] tmpArr = StringUtils.splitByWholeSeparator(key, "_join_");
            group.setJoin(tmpArr[1]);
            key = tmpArr[0];
        }
        if (key.indexOf("_or_") > -1) {
            group.setGroupOperation(CriteriaConditionGroup.GROUP_OPERATION_OR);
            List<CriteriaConditionPojo> entities = buildOrConditionEntity(key, value);
            group.getAndConditions().addAll(entities);
            return group;
        } else {
            group.setGroupOperation(CriteriaConditionGroup.GROUP_OPERATION_AND);
            CriteriaConditionPojo entity;
            if (key.indexOf("_and_") > -1) {
                String[] tmpArr = StringUtils.splitByWholeSeparator(key, "_and_");
                String[] tmpArr2 = StringUtils.splitByWholeSeparator(value.toString(), "##");
                for (int i = 0; i < tmpArr.length; i++) {
                    entity = buildConditionEntity(tmpArr[i], tmpArr2[i]);
                    if (entity != null) {
                        group.getAndConditions().add(entity);
                    }
                }
                return group;
            } else {
                entity = buildConditionEntity(key, value);
                if (entity != null) {
                    group.getAndConditions().add(entity);
                    return group;
                }
            }
        }
        return null;
    }

    private CriteriaConditionPojo buildConditionEntity(String key, Object value) {
        String [] tmpArr = StringUtils.splitByWholeSeparator(key, "_");
        CriteriaConditionPojo entity = new CriteriaConditionPojo();
        switch (tmpArr.length) {
            case 2:
                entity.setOperation(CriteriaOperation.valueOf(tmpArr[0]));
                entity.setField(tmpArr[1]);
                entity.setValue(value);
                return entity;
        }
        return null;
    }

    private List<CriteriaConditionPojo> buildOrConditionEntity(String key, Object value) {
        List<CriteriaConditionPojo> entities = new ArrayList<CriteriaConditionPojo>();
        if (StringUtils.isNotBlank(key) && key.indexOf("_or_") > -1) {
            String[] tmpArr = key.split("_or_");
            String[] tmpArr2 = value.toString().split("##");
            CriteriaConditionPojo entity;
            for (int i = 0; i < tmpArr.length; i++) {
                entity = buildConditionEntity(tmpArr[i], tmpArr2[i]);
                if (entity != null) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    private List<Order> parseOrderBy(CriteriaBuilder builder, Root root, String sort) {
        List<Order> orders = new ArrayList<Order>();
        if (StringUtils.isNotBlank(sort)) {
            String[] arr = sort.split(",");
            for (String oneSort : arr) {
                String[] oneSortArr = oneSort.split(" ");
                if (oneSortArr.length == 2) {
                    String name = oneSortArr[0];
                    String dir = oneSortArr[1];
                    String[] names = StringUtils.splitByWholeSeparator(name, ".");
                    Path path = getPath(root, names);
                    Order order = "asc".equals(dir) ? builder.asc(path) : builder.desc(path);
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    private Path getPath(From from, String[] names) {
        Path expression = from.get(names[0]);
        for (int i = 1; i < names.length; i++) {
            expression = expression.get(names[i]);
        }
        return expression;
    }

    public static Date parseDate(String str) {
        try {
            return DateUtils.parseDate(str, PARSE_PATTERNS.toArray(new String[PARSE_PATTERNS.size()]));
        } catch (ParseException e) {
            throw new RuntimeException("无法解析日期：" + str, e);
        }
    }

}
