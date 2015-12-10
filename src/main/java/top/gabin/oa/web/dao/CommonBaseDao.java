package top.gabin.oa.web.dao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: 甘竞存
 * Date: 14-10-4
 * Time: 下午4:24
 * 为子类提供基础的增删改查方法
 */
public interface CommonBaseDao<SUB, T> {
    SUB findById(Long id);

    SUB saveOrUpdate(SUB t);

    void delete(SUB t);

    void deleteById(Long id);

    List<SUB> findAll();
}
