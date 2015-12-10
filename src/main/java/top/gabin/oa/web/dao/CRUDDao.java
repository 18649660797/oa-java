package top.gabin.oa.web.dao;

import java.util.List;

/**
 * Created by linjiabin on 15/3/25.
 */
public interface CRUDDao {
    <T> T findById(Class<T> clazz, Long id);

    <T> T saveOrUpdate(T t);

    <T> void delete(T t);

    <T> void deleteById(Class<T> clazz, Long id);

    <T> List<T> findAll(Class<T> clazz);
}
