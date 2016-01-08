package top.gabin.oa.web.service.criteria.simple;

import top.gabin.oa.web.dto.PageDTO;

import java.util.List;

/**
 *
 * @author linjiabin  on  16/1/7
 */
public interface QueryService {
    //************************ 主要自己组装条件   ***************************

    <S, B extends  S> S querySingle(Class<S> sClass, Class<B> bClass, QueryCondition queryCondition);

    <S, B extends  S> List<S> query(Class<S> sClass, Class<B> bClass, QueryCondition queryCondition);

    <S, B extends  S> PageDTO<S> pageQuery(Class<S> sClass, Class<B> bClass, PageQueryCondition queryCondition);

    <B> Long count(Class<B> bClass, QueryCondition queryCondition);

    <T extends Number> T sum(Class<T> returnType, SumQueryCondition sumQueryCondition);

}

