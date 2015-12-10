package top.gabin.oa.web.dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;

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
        return (SUB)em.find(clazz,id);
    }

    @Override
    public SUB saveOrUpdate(SUB t){
        return em.merge(t);
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
}
