package top.gabin.oa.web.dao;


import org.apache.commons.lang3.StringUtils;
import top.gabin.oa.web.dto.AttendanceImportDTO;
import top.gabin.oa.web.entity.Department;
import top.gabin.oa.web.entity.DepartmentImpl;
import top.gabin.oa.web.entity.Employee;
import top.gabin.oa.web.entity.EmployeeImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 甘竞存
 * Date: 14-10-4
 * Time: 下午3:09
 */
public abstract class CommonBaseDaoImpl<SUB, T> implements CommonBaseDao<SUB, T> {

    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;
    private Class clazz ;

    public CommonBaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz =(Class)type.getActualTypeArguments()[1];
    }

    @Override
    @SuppressWarnings("unchecked")
    public SUB findById(Long id){
        if (id == null) {
            return null;
        }
        return (SUB)em.find(clazz,id);
    }

    @Override
    public SUB saveOrUpdate(SUB t){
        return em.merge(t);
    }

    @Override
    public void persist(SUB t) {
        em.persist(t);
    }

    @Override
    public void delete(SUB t){
        em.remove(t);
    }

    @Override
    public void deleteById(Long id){
        SUB t = findById(id);
        delete(t);
    }

    @Override
    public List<SUB> findAll() {
        Query query = em.createQuery("from " + clazz.getName());
        return query.getResultList();
    }

    @Override
    public void batchInsert(List<SUB> list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));
            if (i % 1000 == 0) {
                em.flush();
                em.clear();
            }
        }
    }

}
