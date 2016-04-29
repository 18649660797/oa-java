package top.gabin.oa.web.dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    private Class iClazz ;

    public CommonBaseDaoImpl() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        clazz =(Class)type.getActualTypeArguments()[1];
        iClazz =(Class)type.getActualTypeArguments()[0];
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
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(iClazz);
        Root from = query.from(clazz);
        query.select(from);
        TypedQuery typedQuery = em.createQuery(query);
        return typedQuery.getResultList();
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
