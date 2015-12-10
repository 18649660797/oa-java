package top.gabin.oa.web.dao;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * Created by linjiabin on 15/3/25.
 */
@Service("crudDao")
public class CRUDDaoImpl implements CRUDDao {
    @PersistenceContext(name = "entityManagerFactory")
    protected EntityManager em;

    @Override
    public <T> T findById(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }

    @Override
    public <T> T saveOrUpdate(T t) {
        return em.merge(t);
    }

    @Override
    public <T> void delete(T t) {
        em.remove(t);
    }

    @Override
    public <T> void deleteById(Class<T> clazz, Long id) {
        delete(findById(clazz, id));
    }

    @Override
    public <T> List<T> findAll(Class<T> clazz) {
        Query query = em.createQuery("from " + clazz.getName());
        return query.getResultList();
    }
}
